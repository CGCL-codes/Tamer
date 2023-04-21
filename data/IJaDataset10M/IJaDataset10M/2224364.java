package loroedi;

import loroedi.Preferencias;
import loroedi.jedit.AreaTexto_jedit;
import loroedi.Configuracion;
import loroedi.help.doc.DocBrowser;
import util.editor.*;
import loro.EjecucionException;
import loro.*;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;


/////////////////////////////////////////////////////////
/**
 * VIEJO. Se encarga de construir el EDI completo de Loro.
 *
 * @author Carlos Rueda
 * @version 2002-01-22
 */
public class EDI implements EditorListener
{
	private String prefix_title;

	/** La ventana principal. */
	private JFrame frame;

	/** Las acciones sobre el entorno. */
	Acciones acciones;
	
	/** El contenido actual está compilado? */
	boolean compilacionAlDia;
	
	IUnidad[] compilado;

	/** El editor de archivos fuente. */
	Editor editor;
	
	JLabel etq;
	
	JMenu menuAlgoritmos;
	
	/** La toolbar. */
	protected JToolBar tb;
	
	JTextArea texto;

	/** El control . */
	LoroControl loroControl;
	
	
	DocBrowser docBrowser = null;


	////////////////////////////////////////////////////////////////////////////
	/**
	 * Crea el entorno grafico de LoroEDI.
	 */
	public EDI(String prefix_title, JFrame frame_, LoroControl loroControl)
	{
		this.prefix_title = prefix_title;
		this.frame = frame_;
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new java.awt.event.WindowAdapter()
		{
			public void windowClosing(java.awt.event.WindowEvent _)
			{
				salir();
			}
		});

		this.loroControl = loroControl;
		
		acciones = new Acciones(this);

		frame.setJMenuBar(createMenuBar());
		frame.getContentPane().add(createToolBar(), "North");

		// cree el area de texto (aun no tenemos el Editor):
		AreaTexto areaTexto = new AreaTexto_jedit(null);

		// ahora si, cree el Editor (el cual requiere el area de texto!):
		editor = new Editor(
			frame, 
			areaTexto, 
			this,
			"Fuentes Loro (*.loro)",
			".loro"
		);
		areaTexto.ponEditor(editor);

		try
		{
			int pref_font_size = Integer.parseInt(
				Preferencias.obtPreferencia(Preferencias.EDITOR_FONT_SIZE)
			);
			int font_size = areaTexto.getFont().getSize();
			if ( pref_font_size != font_size )
			{
				// directamente al editor para no redundar en la
				// actualizacion de la preferencia:
				editor.changeFontSize(pref_font_size - font_size);
			}
		}
		catch(NumberFormatException ex)
		{
			// ignorado: Normalmente tomar un entero de una preferencia
			// "numerica" operara sin problemas. Pero si llega a fallar,
			// simplemente se deja el tamano por defecto.
		}
		
		
		etq = new JLabel("Listo", SwingConstants.LEFT);

		texto = new JTextArea();
		texto.setBackground(new java.awt.Color(244,221,227));
		texto.setFont(new java.awt.Font("dialog", 0, 12));
		texto.setEditable(false);
		texto.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent e)
			{
				if ( e.getKeyCode() == KeyEvent.VK_ENTER )
				{
					editor.traerAlFrente();
				}
			}
		});

		JScrollPane scrollp = new JScrollPane(
			texto,
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
		);
		scrollp.setMinimumSize(new java.awt.Dimension(100, 60));
		scrollp.setMaximumSize(new java.awt.Dimension(10000, 100));

		JSplitPane split = getJSplitPane0();

		frame.getContentPane().add(etq, "South");

		split.add(editor.comoComponent());
		split.add(scrollp);

		frame.getContentPane().add(split);

		// Manejo preferencia rectangulo sobre la pantalla:
		Rectangle rect = Preferencias.obtRectangulo(Preferencias.EDITOR_RECT);
		frame.setLocation(rect.x, rect.y);
		frame.setSize(rect.width, rect.height);
		frame.addComponentListener(new ComponentAdapter()
		{
			void common()
			{
				Rectangle rect_ = new Rectangle(frame.getLocationOnScreen(), frame.getSize());
				Preferencias.ponRectangulo(Preferencias.EDITOR_RECT, rect_);
			}
			public void componentResized(ComponentEvent e){common();}
			public void componentMoved(ComponentEvent e){common();}
		});

		
		primerArchivo();

	}

	////////////////////////////////////////////////////////////////////////////
	public void display()
	{
		if ( !frame.isShowing() )
		{
			frame.setVisible(true);
		}
		editor.traerAlFrente();
	}

	////////////////////////////////////////////////////////////////////////////
	/**
	 * Crea la barra de menus.
	 */
	JMenuBar createMenuBar()
	{
		JMenuBar mb = new JMenuBar();
		JMenu m;
		m = new JMenu("Archivo");
		m.add(acciones.obtAccion("nuevo"))
			.setAccelerator(
				KeyStroke.getKeyStroke(
					KeyEvent.VK_N, ActionEvent.CTRL_MASK)
			)
		;
		m.add(acciones.obtAccion("abrir"))
			.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0)
			)
		;
		m.add(acciones.obtAccion("guardar"))
			.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0)
			)
		;
		m.add(acciones.obtAccion("guardar-como"));

		m.addSeparator();
		m.add(acciones.obtAccion("salir"))
			.setAccelerator(
				KeyStroke.getKeyStroke(
					KeyEvent.VK_Q, ActionEvent.CTRL_MASK)
			)
		;
		mb.add(m);

		m = new JMenu("Edición");
		m.add(acciones.obtAccion("copiar"))
			.setAccelerator(
				KeyStroke.getKeyStroke(
					KeyEvent.VK_C, ActionEvent.CTRL_MASK)
			)
		;
		m.add(acciones.obtAccion("cortar"))
			.setAccelerator(
				KeyStroke.getKeyStroke(
					KeyEvent.VK_X, ActionEvent.CTRL_MASK)
			)
		;
		m.add(acciones.obtAccion("pegar"))
			.setAccelerator(
				KeyStroke.getKeyStroke(
					KeyEvent.VK_V, ActionEvent.CTRL_MASK)
			)
		;
		m.add(acciones.obtAccion("seleccionar-todo"));
		m.addSeparator();
		m.add(acciones.obtAccion("buscar"))
			.setAccelerator(
				KeyStroke.getKeyStroke(
					KeyEvent.VK_B, ActionEvent.CTRL_MASK)
			)
		;
		m.add(acciones.obtAccion("buscar-siguiente"))
			.setAccelerator(
				KeyStroke.getKeyStroke(
					KeyEvent.VK_S, ActionEvent.CTRL_MASK)
			)
		;
		m.addSeparator();
		m.add(acciones.obtAccion("ir-a-linea"))
			.setAccelerator(
				KeyStroke.getKeyStroke(
					KeyEvent.VK_L, ActionEvent.CTRL_MASK)
			)
		;
		m.addSeparator();
		m.add(acciones.obtAccion("incr-font"));
		m.add(acciones.obtAccion("decr-font"));
		mb.add(m);

		m = new JMenu("Acción");
		m.add(acciones.obtAccion("compilar"))
			.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0)
			)
		;

		menuAlgoritmos = new JMenu("Ejecutar algoritmo");
		menuAlgoritmos.setToolTipText(
			"Permite escoger un algoritmo de la última compilación para su ejecución directa"
		);
		menuAlgoritmos.add(new JLabel("  (NO HAY)  "));
		m.add(menuAlgoritmos);
		m.addSeparator();
		m.add(acciones.obtAccion("mostrar-interprete-interactivo"))
			.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.ALT_MASK)
			)
		;
		mb.add(m);

		m = new JMenu("Ayuda");
		m.add(acciones.obtAccion("ayuda"))
			.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0)
			)
		;
		m.addSeparator();
		m.add(acciones.obtAccion("ayuda-doc"))
			.setAccelerator(
				KeyStroke.getKeyStroke(
					KeyEvent.VK_F1, ActionEvent.CTRL_MASK)
			)
		;
		m.addSeparator();
		m.add(acciones.obtAccion("verificar-sistema"));
		mb.add(m);
		
		m.addSeparator();
		m.add(acciones.obtAccion("a-proposito-de"));
		mb.add(m);

		updateMenus();

		return mb;
	}

	/////////////////////////////////////////////////////////
	/**
	 */
	public void updateMenus()
	{
	}



	////////////////////////////////////////////////////////////////////////////
	/**
	 * Gets the main frame of the application.
	 */
	public JFrame getMainFrame()
	{
		return frame;
	}

	////////////////////////////////////////////////////////////////////////////
	/**
	 * Gets the main content pane.
	 */
	public JPanel getContentPane()
	{
		return (JPanel) frame.getContentPane();
	}

	////////////////////////////////////////////////////////////////////////////
	/**
	 * Sets the main frame's title.
	 */
	public void setTitle(String title)
	{
		frame.setTitle(prefix_title+ " - " +title);
	}


	////////////////////////////////////////////////////////////
	/**
	 */
	class Item extends JMenuItem
	implements ActionListener
	{
		IUnidad u;

		////////////////////////////////////////////////////////////
		Item(IUnidad u)
		{
			super(u.toString());

			// 2001-08-30
			// ToolTipText: primera version: un poco simple: se pone
			// la descripcion de la especificacion y la estrategia del
			// algoritmo. Se utiliza un formato HTML muy preliminar
			// (cuya apariencia no se garantiza mas teniendo en cuenta
			// la version de JVM que se utilice)
			// (Por ejemplo, se deberia reemplazar "\n" -> "<br>")
			//if ( u.esEjecutable() )
			if ( u instanceof IUnidad.IAlgoritmo )
			{
				String tooltip = "<html><pre>";

				String desc_espec = Loro.obtDescripcionEspecificacion(u);
				if ( desc_espec != null )
				{
					tooltip +=
						"<b>Descripci&oacute;n:</b>\n" +
						desc_espec+
						"\n"
					;
				}
				String estrategia = Loro.obtEstrategiaAlgoritmo(u);
				if ( estrategia != null )
				{
					tooltip += "<b>Estrategia:</b>\n" +estrategia;
				}

				tooltip += "\n</pre></html>";
				setToolTipText(tooltip);
			}
			this.u = u;
			addActionListener(this);
		}

		////////////////////////////////////////////////////////////
		public void actionPerformed(ActionEvent e)
		{
			ejecutar(u);
		}
	}



	////////////////////////////////////////////////////////////////
	/**
	 * Interactúa con el usuario para abrir un archivo.
	 */
	protected void abrir()
	{
		etq.setText("Abriendo...");
		editor.abrir();
		etq.setText("Listo");
		Preferencias.ponPreferencia(Preferencias.RECENT, editor.obtNombreArchivo());
	}

	////////////////////////////////////////////////////////////////
	/**
	 * Arma el menú de la lista de los algoritmos
	 * en última compilación para ejecutar alguno de ellos.
	 */
	void actualizarListaAlgoritmos()
	{
		int count = 0;
		menuAlgoritmos.removeAll();
		for (java.util.Enumeration enum = loroControl.obtCompilados();
				enum.hasMoreElements(); )
		{
			IUnidad u = (IUnidad) enum.nextElement();
			menuAlgoritmos.add(new Item(u));
			count++;
		}

		if ( count == 0 )
		{
			menuAlgoritmos.add(new JLabel("  (NO HAY)  "));
		}
	}

	////////////////////////////////////////////////////////////////
	/**
	 * Efectúa la traducción del texto fuente que actualmente
	 * contiene la ventana de edición y muestra el resultado
	 * en la ventana de traducción.
	 */
	void compilar()
	{
		etq.setText("Compilando...");
		etq.paintImmediately(etq.getBounds()); // ?

		String res = "Compilación exitosa";
		compilado = null;
		compilacionAlDia = false;
		try
		{
			IFuente fuente = loroControl.compilar(editor.getText());
			compilado = fuente.obtUnidades();
			etq.setText(res);
			ponTextoMensaje(res);
			compilacionAlDia = true;
		}
		catch(CompilacionException ex)
		{
			//String tex = editor.getText();
			Rango rango = ex.obtRango();
			
			res = "[" +rango.obtIniLin()+ "," +rango.obtIniCol()+ "]" +
				" " +ex.getMessage()
			;


			if ( rango != null )
			{	
				editor.select(rango.obtPosIni(), rango.obtPosFin());
			}

			etq.setText("Hubo error en la compilación");
			ponTextoMensaje(res);
		}

		actualizarListaAlgoritmos();

		editor.traerAlFrente();
	}

	/////////////////////////////////////////////////////////////////
	/**
	 * Crea la tool bar.
	 */
	JToolBar createToolBar()
	{
		JToolBar tb = new JToolBar();
		tb.setFloatable(false);

		tb.add(acciones.obtAccion("nuevo"));
		
		tb.add(acciones.obtAccion("abrir"));
		tb.add(acciones.obtAccion("guardar"));
		tb.add(acciones.obtAccion("guardar-como"));

		tb.addSeparator();
		
		tb.add(acciones.obtAccion("copiar"));
		tb.add(acciones.obtAccion("cortar"));
		tb.add(acciones.obtAccion("pegar"));
		
		tb.addSeparator();
		
		tb.add(acciones.obtAccion("buscar"));
		tb.add(acciones.obtAccion("buscar-siguiente"));

		tb.addSeparator();
		
		tb.add(acciones.obtAccion("compilar"));
		tb.add(acciones.obtAccion("mostrar-interprete-interactivo"));
		tb.add(acciones.obtAccion("ayuda-doc"));
		tb.add(acciones.obtAccion("ayuda"));

		return tb;
	}


	////////////////////////////////////////////////////////////////
	public void estaModificado()
	{
		compilacionAlDia = false;
	}

	////////////////////////////////////////////////////////////////
	/**
	 */
	JSplitPane getJSplitPane0()
	{
		final JSplitPane split0 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		split0.setDividerSize(3);
		split0.setAutoscrolls(false);
		split0.setContinuousLayout(false);
		split0.setMinimumSize(new java.awt.Dimension(100, 100));
		split0.addComponentListener
		(	new ComponentAdapter()
			{
				public void componentResized(ComponentEvent e)
				{
					double h = split0.getSize().getHeight();
					double p = 0.1;
					if ( p * h < 60. )
						p = 60. / h;

					p = 1. -p;
					if ( 0. < p && p < 1.0 )
						split0.setDividerLocation(p);
				}
			}
		);
		return split0;
	}

	////////////////////////////////////////////////////////////////////////////
	/**
	 */
	public void guardar()
	{
		etq.setText("Guardando...");
		etq.paintImmediately(etq.getBounds());
		obtEditor().guardar();
		etq.setText("Guardando...Listo");
		editor.traerAlFrente();
	}

	////////////////////////////////////////////////////////////////////////////
	/**
	 */
	public void mostrarAPropositoDe()
	{
		loroControl.mostrarAPropositoDe();
	}

	////////////////////////////////////////////////////////////////////////////
	/**
	 */
	public void verificarSistema(boolean mostrarMensajeExito)
	{
		loroControl.verificarConfiguracionNucleo(mostrarMensajeExito);
	}

	////////////////////////////////////////////////////////////////////////////
	/**
	 */
	public void mostrarAyuda()
	{
		loroedi.help.HelpManager.displayHelp();
	}

	////////////////////////////////////////////////////////////////////////////
	/**
	 */
	public void mostrarDocumentacion()
	{
		if ( docBrowser == null )
		{
			docBrowser = new DocBrowser(
				Preferencias.obtPreferencia(Preferencias.DOC_DIR),
				".html"
			);
		}
		docBrowser.display();
	}

	////////////////////////////////////////////////////////////////////////////
	/**
	 */
	public void mostrarInterpreteInteractivo()
	{
		loroedi.Interprete.getInstance().mostrar();
	}

	////////////////////////////////////////////////////////////////////////////
	/**
	 * Obtiene el editor de archivos fuente.
	 */
	public Editor obtEditor()
	{
		return editor;
	}

	////////////////////////////////////////////////////////////////////////
	/** Implementación para EditorListener. */
	public void ponDirectorioFuentes(String dir)
	{
		loroControl.ponDirectorioFuentes(dir);
	}

	/////////////////////////////////////////////////////////////////
	public void ponNombreArchivo(String nomArchivo)
	{
		loroControl.ponNombreArchivo(nomArchivo);
		setTitle(nomArchivo);
	}

	////////////////////////////////////////////////////////////////
	/**
	 */
	void ponTextoMensaje(String s)
	{
		texto.setText(s);
		texto.setCaretPosition(0);
	}

	/////////////////////////////////////////////////////////////////
	/**
	 * Revisa preferencia para reciente archivo editado.
	 * Si no hay reciente, se pone el directorio DIR/demo/ de arrancada.
	 */
	void primerArchivo()
	{
		String recent = Preferencias.obtPreferencia(Preferencias.RECENT);
		if ( recent.length() > 0 )
		{
			// abra el reciente si existe:
			if ( new java.io.File(recent).exists() )
			{
				editor.abrirArchivo(recent);
				editor.traerAlFrente();
				return;
			}
		}
		
		// Ponga directorio demo/ de arrancada para abrir archivos fuente.
		String dir_demo = Configuracion.getProperty(Configuracion.DIR)
			+ "/demo"
		;
		editor.ponRutaBase(dir_demo);
		editor.nuevo();
		editor.traerAlFrente();
	}

	////////////////////////////////////////////////////////////////////////////
	/**
	 * Ejecuta la accion de salir del programa.
	 * Esto implica confirmacion con el usuario posiblemente.
	 */
	public void salir()
	{
		if ( editor.confirmarSalir() )
		{
			Rectangle rect = new Rectangle(frame.getLocationOnScreen(), frame.getSize());
			Preferencias.ponRectangulo(Preferencias.EDITOR_RECT, rect);

			loroControl.salir();
		}
	}

	////////////////////////////////////////////////////////////////
	/**
	 */
	void ejecutar(IUnidad u)
	{
		String res = "Listo";

		if ( !loroControl.esEjecutable(u) )
		{
			res = "No es ejecutable " +compilado;
			etq.setText(res);
			ponTextoMensaje(res);
			return;
		}

		etq.setText("Ejecutando...");

		try
		{
			loroControl.ejecutarConConsola(u, null);
			etq.setText(res);
			ponTextoMensaje(res);
		}
		catch(Exception e)
		{
			res = e+ " " +e.getMessage();
			etq.setText("Error inesperado en ejecución");
			ponTextoMensaje(res);
			e.printStackTrace();
		}
	}

	////////////////////////////////////////////////////////////////
	/**
	 * Incrementa o decrementa el tamano del font actual
	 * del editor.
	 */
	public void changeFontSize(int inc)
	{
		editor.changeFontSize(inc);
		int font_size = editor.obtAreaTexto().getFont().getSize();
		Preferencias.ponPreferencia(Preferencias.EDITOR_FONT_SIZE, "" +font_size);
	}
}