package app;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Vector;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class Comunidad extends javax.swing.JFrame {

    {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel jPanel1;

    private JScrollPane jScrollPane1;

    private JButton btnEditar;

    CookiesInJava cook;

    private JButton btnCerrar;

    Config config;

    Main m;

    private JTable tblBorradores;

    private DefaultTableModel modelTable;

    Vector<String[]> lista = new Vector<String[]>();

    ;

    String cuerpo = "";

    String titulo = "";

    String tags = "";

    String categoria = "";

    String sources = "";

    boolean privado = false;

    boolean sin_comentarios = false;

    private JButton btnEliminar;

    /**
	* Auto-generated main method to display this JFrame
	*/
    private void clean() {
        modelTable.setRowCount(0);
    }

    private void refinar() {
        titulo = (String) modelTable.getValueAt(tblBorradores.getSelectedRow(), 0);
        String[] tmp = cuerpo.split("<textarea id=\"markItUp\" name=\"cuerpo\" class=\"required\" style=\"height: 500px\" tabindex=\"2\">");
        cuerpo = tmp[1].replaceAll("</textarea>", "");
        cuerpo = cuerpo.replaceAll("&quot;", "\\\"");
        cuerpo = cuerpo.replaceAll("&amp;quot;", "\\\"");
        tmp = tags.split("<input class=\"text-inp required\" type=\"text\" name=\"tags\" value=\"");
        tags = tmp[1].replaceAll("\" />", "");
        tmp = categoria.split("value=\"");
        categoria = tmp[1].substring(0, tmp[1].indexOf('"'));
    }

    private void refinarv5() {
        titulo = (String) modelTable.getValueAt(tblBorradores.getSelectedRow(), 0);
        String[] tmp = cuerpo.split("<textarea id=\"markItUp\" name=\"cuerpo\" class=\"post required text cuenta-save-1 ui-corner-all form-input-text box-shadow-soft\" style=\"height: 500px; resize: none;\" tabindex=\"2\">");
        cuerpo = tmp[1].replaceAll("</textarea>", "");
        System.out.println(categoria);
    }

    public void obtenerBorrador(String key) throws Exception {
        cook.clearCookies();
        lista.clear();
        String url = "http://www.taringa.net/agregar/" + key;
        String line;
        HttpURLConnection conn;
        URL siteUrl;
        String respuesta;
        BufferedReader in;
        m.login(config.getUsuario(), config.getContrasenia());
        siteUrl = new URL(url);
        conn = (HttpURLConnection) siteUrl.openConnection();
        conn.setRequestMethod("GET");
        cook.writeCookies(conn, false);
        conn.setDoOutput(true);
        in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        line = "";
        respuesta = "";
        while ((line = in.readLine()) != null) {
            if (line.contains(("<label>Contenido del Post</label>"))) {
                cuerpo = cuerpo + line.trim() + "\n";
                while (!(line = in.readLine()).contains("</textarea>")) {
                    cuerpo = cuerpo + line.trim() + "\n";
                }
                if (!line.contains("Post creado")) cuerpo = cuerpo + line.trim() + "\n";
                ;
            }
            if (line.contains((" name=\"tags\" "))) tags = line.trim();
            if (line.contains("var sourcesData =")) sources = line.trim();
            if (line.contains(("\" selected=\"selected\">")) && !line.contains("value=\"root\" selected=\"selected\"")) categoria = line.trim();
            if (line.contains((" name=\"privado\"")) && line.contains("\"checked=\"1\"")) privado = true;
            if (line.contains((" name=\"sin_comentarios\" ")) && line.contains("\"checked=\"1\"")) sin_comentarios = true;
        }
        in.close();
    }

    public void obtenerBorradorV5(String key) throws Exception {
        cook.clearCookies();
        lista.clear();
        cook.addCookie("canIHaveBetaForTaringaV5Plskthx", "79f11ff2c8fab868d73d72269119f3b8", true);
        String url = "http://www.taringa.net/agregar/" + key;
        String line;
        HttpURLConnection conn;
        URL siteUrl;
        String respuesta;
        BufferedReader in;
        System.out.println(config.getUsuario());
        m.login(config.getUsuario(), config.getContrasenia());
        siteUrl = new URL(url);
        conn = (HttpURLConnection) siteUrl.openConnection();
        conn.setRequestMethod("GET");
        cook.writeCookies(conn, false);
        conn.setDoOutput(true);
        in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        line = "";
        respuesta = "";
        while ((line = in.readLine()) != null) {
            if (line.contains((" name=\"cuerpo\" "))) {
                cuerpo = cuerpo + line.trim() + "\n";
                while (!(line = in.readLine()).contains("</textarea>")) {
                    cuerpo = cuerpo + line.trim() + "\n";
                }
                cuerpo = cuerpo + line.trim() + "\n";
            }
            if (line.contains(("<textarea name=\"tags\""))) tags = line.trim();
            if (line.contains("var sourcesData =")) sources = line.trim();
            if (line.contains(("\" selected=\"selected\">")) && !line.contains("value=\"root\" selected=\"selected\"")) categoria = line.trim();
            if (line.contains((" name=\"sin_comentarios\" ")) && line.contains("\"checked=\"1\"")) sin_comentarios = true;
        }
        in.close();
        System.out.println(tags);
    }

    public Comunidad(CookiesInJava cook, Config config, Main main) {
        super();
        this.config = config;
        this.cook = cook;
        m = main;
        initGUI();
    }

    public void mostrarBorradores() {
        this.setVisible(true);
        this.setTitle("Mis borradores de T! - " + config.getUsuario());
    }

    public void cortar(String str) {
        if (!str.equals("")) {
            String[] info = str.split(",");
            String[] temp = null;
            temp = new String[4];
            temp[3] = info[0].replaceAll("[^0-9]", "");
            ;
            info[1] = HtmlEntities.decode(info[1]);
            temp[0] = info[1].substring(0, info[1].length() - 1).replaceAll("\"titulo\":\"", "").replaceAll("\\/", "").replace('\\', '/').replaceAll("&quot;", "\"");
            temp[1] = info[7].substring(0, info[7].length() - 1).replaceAll("\"categoria_name\":\"", "");
            temp[2] = info[10].substring(0, info[10].length() - 1).replaceAll("\"fecha_print\":\"", "").replaceAll("\\/", "").replace('\\', '-');
            lista.add(temp);
        }
    }

    public void getBorradoresOnline() throws Exception {
        cook.clearCookies();
        lista.clear();
        String url = "http://www.taringa.net/mis-borradores";
        String line;
        HttpURLConnection conn;
        URL siteUrl;
        String respuesta;
        BufferedReader in;
        System.out.println(config.getUsuario());
        m.login(config.getUsuario(), config.getContrasenia());
        siteUrl = new URL(url);
        conn = (HttpURLConnection) siteUrl.openConnection();
        conn.setRequestMethod("GET");
        cook.writeCookies(conn, false);
        conn.setDoOutput(true);
        in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        line = "";
        respuesta = "";
        while ((line = in.readLine()) != null) if (line.contains((" var borradores_data = "))) respuesta = line.trim();
        in.close();
        System.out.println(respuesta);
        int inicio = respuesta.indexOf("[") + 1;
        int fin = respuesta.length() - 2;
        respuesta = respuesta.substring(inicio, fin);
        String[] borrador = respuesta.split("},");
        for (int i = 0; i < borrador.length; i++) {
            if (!(borrador[i].contains("origen\":\"tema\"") | borrador[i].contains("\"causa\":\"Eliminado"))) cortar(borrador[i]);
        }
        for (int i = 0; i < lista.size(); i++) {
            System.out.println(i + ":" + Arrays.toString(lista.get(i)));
        }
        rellenarTabla();
        conn.disconnect();
    }

    private void rellenarTabla() {
        clean();
        for (int i = 0; i < lista.size(); i++) modelTable.addRow(lista.get(i));
    }

    private void initGUI() {
        try {
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            this.setIconImage(new ImageIcon(getClass().getClassLoader().getResource("app/postinga16x16.jpg")).getImage());
            {
                jPanel1 = new JPanel();
                getContentPane().add(jPanel1, BorderLayout.CENTER);
                jPanel1.setPreferredSize(new java.awt.Dimension(616, 499));
                {
                    tblBorradores = new JTable() {

                        public boolean isCellEditable(int rowIndex, int vColIndex) {
                            return false;
                        }
                    };
                    jScrollPane1 = new JScrollPane();
                    jPanel1.add(jScrollPane1);
                    jScrollPane1.setPreferredSize(new java.awt.Dimension(652, 400));
                    jScrollPane1.setViewportView(tblBorradores);
                    modelTable = new DefaultTableModel();
                    modelTable.addColumn("Titulo");
                    modelTable.addColumn("Categoria");
                    modelTable.addColumn("Ultima modificacion");
                    modelTable.addColumn("ID");
                    tblBorradores.setFillsViewportHeight(true);
                    tblBorradores.setModel(modelTable);
                    tblBorradores.setFont(new java.awt.Font("Arial", 0, 12));
                    tblBorradores.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
                    TableColumn column = null;
                    for (int i = 0; i < 3; i++) {
                        column = tblBorradores.getColumnModel().getColumn(i);
                        if (i == 0) column.setPreferredWidth(280);
                        if (i == 1) column.setPreferredWidth(90);
                        if (i == 2) column.setPreferredWidth(140);
                        if (i == 3) column.setPreferredWidth(10);
                    }
                }
                {
                }
                {
                    btnEditar = new JButton();
                    jPanel1.add(btnEditar);
                    btnEditar.setText("Editar");
                    btnEditar.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent evt) {
                            btnEditarActionPerformed(evt);
                        }
                    });
                }
                {
                    btnCerrar = new JButton();
                    jPanel1.add(btnCerrar);
                    btnCerrar.setText("Cerrar");
                    btnCerrar.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent evt) {
                            btnCerrarActionPerformed(evt);
                        }
                    });
                }
                {
                    btnEliminar = new JButton();
                    jPanel1.add(btnEliminar);
                    btnEliminar.setText("Eliminar");
                    btnEliminar.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent evt) {
                            btnEliminarActionPerformed(evt);
                        }
                    });
                }
            }
            pack();
            this.setLocationRelativeTo(m);
            this.setSize(681, 480);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btnCerrarActionPerformed(ActionEvent evt) {
        this.dispose();
    }

    public boolean existeBorrador(String id) throws Exception {
        if (modelTable.getRowCount() == 0) getBorradoresOnline();
        for (int i = 0; i < modelTable.getRowCount(); i++) {
            if (modelTable.getValueAt(i, 0).equals(id)) return true;
        }
        return false;
    }

    private void eliminarBorrador(String id) throws Exception {
        int i = JOptionPane.showConfirmDialog(this, "¿Estas seguro de eliminar el post " + modelTable.getValueAt(tblBorradores.getSelectedRow(), 0) + "?", "Eliminar post", JOptionPane.YES_NO_OPTION);
        if (i == 0) {
            cook.clearCookies();
            lista.clear();
            DataOutputStream out;
            String url = "http://www.taringa.net/borradores-eliminar.php";
            String line;
            HttpURLConnection conn;
            URL siteUrl;
            String contenido = "borrador_id=" + id + "&borrador_tipo=post";
            BufferedReader in;
            m.login(config.getUsuario(), config.getContrasenia());
            siteUrl = new URL(url);
            conn = (HttpURLConnection) siteUrl.openConnection();
            conn.setRequestMethod("POST");
            cook.writeCookies(conn, false);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(contenido);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            line = "";
            line = in.readLine();
            System.out.println(line);
            if (line.contains("OK")) {
                System.out.println("El borrador fue eliminado");
                JOptionPane.showMessageDialog(this, "El borrador fue eliminado", "Eliminar borrador", JOptionPane.PLAIN_MESSAGE);
                modelTable.removeRow(tblBorradores.getSelectedRow());
            }
            in.close();
            out.close();
        }
    }

    private void btnEditarActionPerformed(ActionEvent evt) {
        try {
            if (tblBorradores.getSelectedRow() > -1) {
                obtenerBorrador((String) modelTable.getValueAt(tblBorradores.getSelectedRow(), 3));
                refinar();
                m.setTitulo(titulo);
                m.setCuerpo(cuerpo);
                m.setTags(tags);
                m.setCategoria(categoria);
                m.setBorradorId((String) modelTable.getValueAt(tblBorradores.getSelectedRow(), 3));
                ((Main) m).setPrivado(privado);
                ((Main) m).setSin_comentarios(sin_comentarios);
                m.setEditorCaretPosition(0);
                m.editorRequestFocus();
                m.tituloTagsFuentesNormales();
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No hay ningun post seleccionado", "Editar borrador", JOptionPane.PLAIN_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btnEliminarActionPerformed(ActionEvent evt) {
        if (tblBorradores.getSelectedRow() > -1) {
            try {
                eliminarBorrador(((String) modelTable.getValueAt(tblBorradores.getSelectedRow(), 3)));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "No se pudo establecer la conexión con el servidor.\n Chequea tu conexión a internet", "Ups", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No hay ningun post seleccionado", "Eliminar borrador", JOptionPane.PLAIN_MESSAGE);
        }
    }
}
