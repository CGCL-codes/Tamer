package net.juantxu.pentaho.launcher;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.apache.log4j.Logger;

public class Actualizador extends JDialog {

    private static final long serialVersionUID = 1L;

    static Logger log = Logger.getLogger(Actualizador.class);

    static ResourceBundle messages;

    String ejecutableDir = "";

    boolean ejecutableExito = false;

    TextArea expl;

    boolean bloqueado = false;

    boolean cormprobarInicio = false;

    final Properties pr = new CargaProperties().Carga();

    public Actualizador(JFrame frame, ArrayList<String> lista) {
        super(frame, ponTitulo());
        setAlwaysOnTop(true);
        setResizable(true);
        setIconImage(new ImageIcon("icons/pl.png").getImage());
        Container cp = getContentPane();
        int cantidad = lista.size();
        setSize(780, 160 + 25 * cantidad);
        JPanel p2 = new JPanel();
        p2.setLayout(new GridLayout(cantidad + 1, 3));
        cp.setLayout(new BorderLayout());
        cp.add(p2, BorderLayout.NORTH);
        Font titulo = new Font("Dialog", Font.BOLD, 12);
        expl = new TextArea("", 4, 3, TextArea.SCROLLBARS_VERTICAL_ONLY);
        expl.setEditable(false);
        expl.setFont(titulo);
        cp.add(expl, BorderLayout.SOUTH);
        Iterator secuencia = lista.iterator();
        while (secuencia.hasNext()) {
            log.info("apps a actualizar " + (String) secuencia.next());
        }
        if (lista.contains("dataIntegrationRelativePath")) {
            p2.add(new JLabel(messages.getString("lpdi")));
            final JButton kettle = new JButton(messages.getString("buscarPdi"));
            kettle.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    log.debug("buscando en kettle");
                    info("kettleBuscarClick");
                    bloqueado = true;
                    try {
                        ejecutableDir = buscarArchivo(System.getProperty("user.dir"));
                        log.debug("el direcotrio de kettle es" + ejecutableDir);
                        bloqueado = false;
                    } catch (IOException e1) {
                        log.error("no se ha podido coger el directorio");
                        e1.printStackTrace();
                    } finally {
                        ejecutableExito = new CompruebaAplicaciones().validaEjecutables("dataIntegrationRelativePath", ejecutableDir);
                        if (ejecutableExito) {
                            kettle.setEnabled(false);
                            new EscribeProperties("dataIntegrationRelativePath", ejecutableDir);
                        } else {
                            kettle.setText(messages.getString("noTa") + messages.getString("buscarPdi"));
                        }
                    }
                }
            });
            kettle.addMouseListener(new MouseAdapter() {

                public void mouseEntered(MouseEvent e) {
                    if (!bloqueado) {
                        info("kettleBuscarInfo");
                    }
                }
            });
            final JButton kettleDescarga = new JButton(messages.getString("descargarPdi"));
            kettleDescarga.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    log.debug("descargando en kettle");
                    info("kettleDescargaClick");
                    bloqueado = true;
                    new DescargaEInstalaPentahoApp("kettle");
                    bloqueado = false;
                    info("descargaCompletada");
                    kettleDescarga.setEnabled(false);
                    kettle.setEnabled(false);
                }
            });
            kettleDescarga.addMouseListener(new MouseAdapter() {

                public void mouseEntered(MouseEvent e) {
                    if (!bloqueado) {
                        info("kettleDescargaInfo");
                    }
                }
            });
            p2.add(kettle);
            p2.add(kettleDescarga);
        }
        if (lista.contains("workbenchRelativePath")) {
            p2.add(new JLabel(messages.getString("lworkbench")));
            final JButton workbench = new JButton(messages.getString("buscarWorkbench"));
            workbench.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    log.debug("apretando en workbench");
                    info("workbenchBuscarClick");
                    bloqueado = true;
                    try {
                        ejecutableDir = buscarArchivo(System.getProperty("user.dir"));
                        log.debug("el direcotrio de workbench es" + ejecutableDir);
                        bloqueado = false;
                    } catch (IOException e1) {
                        log.error("no se ha podido coger el directorio");
                        e1.printStackTrace();
                    } finally {
                        ejecutableExito = new CompruebaAplicaciones().validaEjecutables("workbenchRelativePath", ejecutableDir);
                        if (ejecutableExito) {
                            workbench.setEnabled(false);
                            new EscribeProperties("workbenchRelativePath", ejecutableDir);
                        } else {
                            workbench.setText(messages.getString("noTa") + messages.getString("buscarPdi"));
                        }
                    }
                }
            });
            workbench.addMouseListener(new MouseAdapter() {

                public void mouseEntered(MouseEvent e) {
                    if (!bloqueado) {
                        info("workbenchBuscarInfo");
                    }
                }
            });
            final JButton workbenchDescarga = new JButton(messages.getString("descargarWorkbench"));
            workbenchDescarga.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    log.debug("descargando workbench");
                    info("workbenchDescargaClick");
                    bloqueado = true;
                    new DescargaEInstalaPentahoApp("workbench");
                    bloqueado = false;
                    info("descargaCompletada");
                    workbenchDescarga.setEnabled(false);
                    workbench.setEnabled(false);
                }
            });
            workbenchDescarga.addMouseListener(new MouseAdapter() {

                public void mouseEntered(MouseEvent e) {
                    if (!bloqueado) {
                        info("workbenchDescargaInfo");
                    }
                }
            });
            p2.add(workbench);
            p2.add(workbenchDescarga);
        }
        if (lista.contains("aggregationRelativePath")) {
            p2.add(new JLabel(messages.getString("laggregation")));
            final JButton aggregation = new JButton(messages.getString("buscarAggregation"));
            aggregation.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    log.debug("apretando en aggregation");
                    info("aggregationBuscarClick");
                    bloqueado = true;
                    try {
                        ejecutableDir = buscarArchivo(System.getProperty("user.dir"));
                        log.debug("el direcotrio de Aggregation designer  es" + ejecutableDir);
                        bloqueado = false;
                    } catch (IOException e1) {
                        log.error("no se ha podido coger el directorio");
                        e1.printStackTrace();
                    } finally {
                        ejecutableExito = new CompruebaAplicaciones().validaEjecutables("aggregationRelativePath", ejecutableDir);
                        if (ejecutableExito) {
                            aggregation.setEnabled(false);
                            new EscribeProperties("aggregationRelativePath", ejecutableDir);
                        } else {
                            aggregation.setText(messages.getString("noTa") + messages.getString("buscarAggregation"));
                        }
                    }
                }
            });
            aggregation.addMouseListener(new MouseAdapter() {

                public void mouseEntered(MouseEvent e) {
                    if (!bloqueado) {
                        info("aggregationBuscarInfo");
                    }
                }
            });
            final JButton aggregationDescarga = new JButton(messages.getString("descargarAggregation"));
            aggregationDescarga.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    log.debug("descargando aggregation desinger");
                    info("aggregationDescargaClick");
                    bloqueado = true;
                    new DescargaEInstalaPentahoApp("aggregation");
                    bloqueado = false;
                    info("descargaCompletada");
                    aggregationDescarga.setEnabled(false);
                    aggregation.setEnabled(false);
                }
            });
            aggregationDescarga.addMouseListener(new MouseAdapter() {

                public void mouseEntered(MouseEvent e) {
                    if (!bloqueado) {
                        info("aggregationDescargaInfo");
                    }
                }
            });
            p2.add(aggregation);
            p2.add(aggregationDescarga);
        }
        if (lista.contains("metadataRelativePath")) {
            p2.add(new JLabel(messages.getString("lmetadata")));
            final JButton metadata = new JButton(messages.getString("buscarMetadata"));
            metadata.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    log.debug("apretando en metadata");
                    info("metadataBuscarClick");
                    bloqueado = true;
                    try {
                        ejecutableDir = buscarArchivo(System.getProperty("user.dir"));
                        log.debug("el direcotrio de metadata es" + ejecutableDir);
                        bloqueado = false;
                    } catch (IOException e1) {
                        log.error("no se ha podido coger el directorio");
                        e1.printStackTrace();
                    } finally {
                        ejecutableExito = new CompruebaAplicaciones().validaEjecutables("metadataRelativePath", ejecutableDir);
                        if (ejecutableExito) {
                            metadata.setEnabled(false);
                            new EscribeProperties("metadataRelativePath", ejecutableDir);
                        } else {
                            metadata.setText(messages.getString("noTa") + messages.getString("buscarMetadata"));
                        }
                    }
                }
            });
            metadata.addMouseListener(new MouseAdapter() {

                public void mouseEntered(MouseEvent e) {
                    if (!bloqueado) {
                        info("metadataBuscarInfo");
                    }
                }
            });
            final JButton metadataDescarga = new JButton(messages.getString("descargarMetadata"));
            metadataDescarga.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    log.debug("descargando metadata");
                    info("metadataDescargaClick");
                    bloqueado = true;
                    new DescargaEInstalaPentahoApp("metadata");
                    bloqueado = false;
                    info("descargaCompletada");
                    metadataDescarga.setEnabled(false);
                    metadata.setEnabled(false);
                }
            });
            metadataDescarga.addMouseListener(new MouseAdapter() {

                public void mouseEntered(MouseEvent e) {
                    if (!bloqueado) {
                        info("metadataDescargaInfo");
                    }
                }
            });
            p2.add(metadata);
            p2.add(metadataDescarga);
        }
        if (lista.contains("reportDesingerRelativePath")) {
            p2.add(new JLabel(messages.getString("lrd")));
            final JButton rd = new JButton(messages.getString("buscarRD"));
            rd.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    log.debug("apretando en report designer");
                    info("rdBuscarClick");
                    bloqueado = true;
                    try {
                        ejecutableDir = buscarArchivo(System.getProperty("user.dir"));
                        log.debug("el direcotrio de report designer es" + ejecutableDir);
                        bloqueado = false;
                    } catch (IOException e1) {
                        log.error("no se ha podido coger el directorio");
                        e1.printStackTrace();
                    } finally {
                        ejecutableExito = new CompruebaAplicaciones().validaEjecutables("reportDesingerRelativePath", ejecutableDir);
                        if (ejecutableExito) {
                            rd.setEnabled(false);
                            new EscribeProperties("reportDesingerRelativePath", ejecutableDir);
                        } else {
                            rd.setText(messages.getString("noTa") + messages.getString("buscarRD"));
                        }
                    }
                }
            });
            rd.addMouseListener(new MouseAdapter() {

                public void mouseEntered(MouseEvent e) {
                    if (!bloqueado) {
                        info("rdBuscarInfo");
                    }
                }
            });
            final JButton rdDescarga = new JButton(messages.getString("descargarRD"));
            rdDescarga.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    log.debug("descargando Report Designer");
                    info("rdDescargaClick");
                    bloqueado = true;
                    new DescargaEInstalaPentahoApp("reportDesigner");
                    bloqueado = false;
                    info("descargaCompletada");
                    rdDescarga.setEnabled(false);
                    rd.setEnabled(false);
                }
            });
            rdDescarga.addMouseListener(new MouseAdapter() {

                public void mouseEntered(MouseEvent e) {
                    if (!bloqueado) {
                        info("rdDescargaInfo");
                    }
                }
            });
            p2.add(rd);
            p2.add(rdDescarga);
        }
        if (lista.contains("designStudioRelativePathNix")) {
            p2.add(new JLabel(messages.getString("lds")));
            final JButton dsNix = new JButton(messages.getString("buscarDS"));
            dsNix.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    log.debug("apretando en design studio");
                    info("dsBuscarClick");
                    bloqueado = true;
                    try {
                        ejecutableDir = buscarArchivo(System.getProperty("user.dir"));
                        bloqueado = false;
                        log.debug("el direcotrio de report designer es" + ejecutableDir);
                    } catch (IOException e1) {
                        log.error("no se ha podido coger el directorio");
                        e1.printStackTrace();
                    } finally {
                        ejecutableExito = new CompruebaAplicaciones().validaEjecutables("designStudioRelativePathNix", ejecutableDir);
                        if (ejecutableExito) {
                            dsNix.setEnabled(false);
                            new EscribeProperties("designStudioRelativePathNix", ejecutableDir);
                        } else {
                            dsNix.setText(messages.getString("noTa") + messages.getString("buscarDS"));
                        }
                    }
                }
            });
            dsNix.addMouseListener(new MouseAdapter() {

                public void mouseEntered(MouseEvent e) {
                    if (!bloqueado) {
                        info("dsBuscarInfo");
                    }
                }
            });
            final JButton dsDescarga = new JButton(messages.getString("descargarDS"));
            dsDescarga.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    log.debug("descargando  Design studio");
                    info("dsDescargaClick");
                    bloqueado = true;
                    new DescargaEInstalaPentahoApp("designStudio");
                    bloqueado = false;
                    info("descargaCompletada");
                    dsDescarga.setEnabled(false);
                    dsNix.setEnabled(false);
                }
            });
            dsDescarga.addMouseListener(new MouseAdapter() {

                public void mouseEntered(MouseEvent e) {
                    if (!bloqueado) {
                        info("dsDescargaInfo");
                    }
                }
            });
            p2.add(dsNix);
            p2.add(dsDescarga);
        }
        if (lista.contains("designStudioRelativePathWin")) {
            p2.add(new JLabel(messages.getString("lds")));
            final JButton dsWin = new JButton(messages.getString("buscarDS"));
            dsWin.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    log.debug("apretando en design studio win");
                    info("dsBuscarClick");
                    bloqueado = true;
                    try {
                        ejecutableDir = buscarArchivo(System.getProperty("user.dir"));
                        bloqueado = false;
                        log.debug("el direcotrio de report designer es" + ejecutableDir);
                    } catch (IOException e1) {
                        log.error("no se ha podido coger el directorio");
                        e1.printStackTrace();
                    } finally {
                        ejecutableExito = new CompruebaAplicaciones().validaEjecutables("designStudioRelativePathWin", ejecutableDir);
                        if (ejecutableExito) {
                            dsWin.setEnabled(false);
                            new EscribeProperties("designStudioRelativePathWin", ejecutableDir);
                        } else {
                            dsWin.setText(messages.getString("noTa") + messages.getString("buscarDS"));
                        }
                    }
                }
            });
            dsWin.addMouseListener(new MouseAdapter() {

                public void mouseEntered(MouseEvent e) {
                    if (!bloqueado) {
                        info("dsBuscarInfo");
                    }
                }
            });
            final JButton dsDescargaWin = new JButton(messages.getString("descargarDS"));
            dsDescargaWin.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    log.debug("descargando  Design studio");
                    info("dsDescargaClick");
                    bloqueado = true;
                    new DescargaEInstalaPentahoApp("designStudio");
                    bloqueado = false;
                    info("descargaCompletada");
                    dsDescargaWin.setEnabled(false);
                    dsWin.setEnabled(false);
                }
            });
            p2.add(dsWin);
            p2.add(dsDescargaWin);
        }
        if (lista.contains("wekaRelativePath")) {
            p2.add(new JLabel(messages.getString("lw")));
            final JButton weka = new JButton(messages.getString("buscarWeka"));
            weka.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    log.debug("apretando en Weka");
                    info("wekaBuscarClick");
                    bloqueado = true;
                    try {
                        ejecutableDir = buscarArchivo(System.getProperty("user.dir"));
                        bloqueado = false;
                        log.debug("el direcotrio de weka es" + ejecutableDir);
                    } catch (IOException e1) {
                        log.error("no se ha podido coger el directorio");
                        e1.printStackTrace();
                    } finally {
                        ejecutableExito = new CompruebaAplicaciones().validaEjecutables("wekaRelativePath", ejecutableDir);
                        if (ejecutableExito) {
                            weka.setEnabled(false);
                            new EscribeProperties("wekaRelativePath", ejecutableDir);
                        } else {
                            weka.setText(messages.getString("noTa") + messages.getString("buscarWeka"));
                        }
                    }
                }
            });
            weka.addMouseListener(new MouseAdapter() {

                public void mouseEntered(MouseEvent e) {
                    if (!bloqueado) {
                        info("wekaBuscarInfo");
                    }
                }
            });
            final JButton wekaDescarga = new JButton(messages.getString("descargarWeka"));
            wekaDescarga.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    log.debug("descargando Weka");
                    info("wekaDescargaClick");
                    bloqueado = true;
                    new DescargaEInstalaPentahoApp("weka");
                    bloqueado = false;
                    info("descargaCompletada");
                    wekaDescarga.setEnabled(false);
                    weka.setEnabled(false);
                }
            });
            wekaDescarga.addMouseListener(new MouseAdapter() {

                public void mouseEntered(MouseEvent e) {
                    if (!bloqueado) {
                        info("wekaDescargaInfo");
                    }
                }
            });
            p2.add(weka);
            p2.add(wekaDescarga);
        }
        if (lista.contains("biserverRelativePath")) {
            p2.add(new JLabel(messages.getString("lbi")));
            final JButton bis = new JButton(messages.getString("buscarBIS"));
            bis.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    log.debug("apretando en BI server");
                    bloqueado = true;
                    info("biBuscarClick");
                    try {
                        ejecutableDir = buscarArchivo(System.getProperty("user.dir"));
                        bloqueado = false;
                        log.debug("el direcotrio de report designer es" + ejecutableDir);
                    } catch (IOException e1) {
                        log.error("no se ha podido coger el directorio");
                        e1.printStackTrace();
                    } finally {
                        ejecutableExito = new CompruebaAplicaciones().validaEjecutables("biserverRelativePath", ejecutableDir);
                        if (ejecutableExito) {
                            bis.setEnabled(false);
                            new EscribeProperties("biserverRelativePath", ejecutableDir);
                        } else {
                            bis.setText(messages.getString("noTa") + messages.getString("buscarBIS"));
                        }
                    }
                }
            });
            bis.addMouseListener(new MouseAdapter() {

                public void mouseEntered(MouseEvent e) {
                    if (!bloqueado) {
                        info("biBuscarInfo");
                    }
                }
            });
            final JButton bisDescarga = new JButton(messages.getString("descargarBIS"));
            bisDescarga.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    bloqueado = true;
                    log.debug("descargando  BI Server");
                    info("biDescargaClick");
                    Object[] options = { messages.getString("si"), messages.getString("no") };
                    int seleccion = JOptionPane.showOptionDialog(bisDescarga, messages.getString("desExtras"), messages.getString("infoExtras"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
                    new DescargaEInstalaPentahoApp("bis");
                    if (seleccion == 0) {
                        log.debug("Descargando extras");
                        new DescargaEInstalaPentahoApp("extra");
                    } else {
                        log.debug("No descargando extras");
                    }
                    bloqueado = false;
                    info("descargaCompletada");
                    bisDescarga.setEnabled(false);
                    bis.setEnabled(false);
                }
            });
            bisDescarga.addMouseListener(new MouseAdapter() {

                public void mouseEntered(MouseEvent e) {
                    if (!bloqueado) {
                        info("biDescargaInfo");
                    }
                }
            });
            p2.add(bis);
            p2.add(bisDescarga);
        }
        if (lista.contains("adminConsoleRelativePath")) {
            p2.add(new JLabel(messages.getString("lac")));
            final JButton pac = new JButton(messages.getString("buscarPAC"));
            pac.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    log.debug("apretando en Pentaho Admin Console");
                    info("acBuscarClick");
                    bloqueado = true;
                    try {
                        ejecutableDir = buscarArchivo(System.getProperty("user.dir"));
                        log.debug("el direcotrio de report designer es" + ejecutableDir);
                    } catch (IOException e1) {
                        log.error("no se ha podido coger el directorio");
                        e1.printStackTrace();
                    } finally {
                        ejecutableExito = new CompruebaAplicaciones().validaEjecutables("adminConsoleRelativePath", ejecutableDir);
                        if (ejecutableExito) {
                            pac.setEnabled(false);
                            new EscribeProperties("adminConsoleRelativePath", ejecutableDir);
                        } else {
                            pac.setText(messages.getString("noTa") + messages.getString("buscarPAC"));
                        }
                    }
                }
            });
            pac.addMouseListener(new MouseAdapter() {

                public void mouseEntered(MouseEvent e) {
                    if (!bloqueado) {
                        info("acBuscarInfo");
                    }
                }
            });
            final JButton pacDescarga = new JButton(messages.getString("descargarPAC"));
            pacDescarga.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    log.debug("descargando Pentaho Admin Console");
                    if (!bloqueado) {
                        info("acDescargaClick");
                    }
                }
            });
            pacDescarga.addMouseListener(new MouseAdapter() {

                public void mouseEntered(MouseEvent e) {
                    if (!bloqueado) {
                        info("acDescargaInfo");
                    }
                }
            });
            p2.add(pac);
            p2.add(pacDescarga);
        }
        p2.add(new JLabel(messages.getString("cerrarEstaVentana")));
        JButton ok = new JButton("OK");
        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        p2.add(ok);
        if (pr.getProperty("coprobarAlInicio").equals("true")) cormprobarInicio = true;
        final JCheckBox cb1 = new JCheckBox(messages.getString("noComprobarInicio"), cormprobarInicio);
        cb1.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                if (!bloqueado) {
                    info("noComprobarInicioExpl");
                }
            }
        });
        cb1.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                cormprobarInicio = (e.getStateChange() == ItemEvent.SELECTED);
                if (cormprobarInicio) {
                    log.debug("actualizando  a true con " + cormprobarInicio);
                    new EscribeProperties("coprobarAlInicio", "true");
                } else {
                    log.debug("actualizando a false con " + cormprobarInicio);
                    new EscribeProperties("coprobarAlInicio", "false");
                }
            }
        });
        p2.add(cb1);
    }

    private static String ponTitulo() {
        Properties pr = new CargaProperties().Carga();
        i18(pr.getProperty("idioma"), pr.getProperty("pais"));
        String titulo = messages.getString("tituloNoEncontrado");
        return titulo;
    }

    private static void i18(String idioma, String pais) {
        Locale currentLocale;
        currentLocale = new Locale(idioma, pais);
        messages = ResourceBundle.getBundle("mensajes", currentLocale);
    }

    private String buscarArchivo(String Path) throws IOException {
        File file = new File(Path);
        JFileChooser fileopen = new JFileChooser();
        fileopen.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int ret = fileopen.showDialog(null, "Open file");
        if (ret == JFileChooser.APPROVE_OPTION) {
            file = fileopen.getSelectedFile();
            System.out.println(file);
        }
        return file.getAbsolutePath();
    }

    private void info(String clave) {
        expl.setText(messages.getString(clave));
    }
}
