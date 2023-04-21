package gpahh;

import gpahh.objetos.ActividadComplementaria;
import gpahh.objetos.Conexion;
import gpahh.objetos.Falta;
import gpahh.objetos.FaltaHora;
import gpahh.objetos.Horario;
import gpahh.objetos.Retardo;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import javax.swing.ImageIcon;

/**
 *
 * @author  s
 */
public class DondeEsta extends javax.swing.JFrame {

    GregorianCalendar cal = new GregorianCalendar();

    int diaActual = cal.get(Calendar.DAY_OF_MONTH);

    int mesActual = cal.get(Calendar.MONTH) + 1;

    int añoActual = cal.get(Calendar.YEAR);

    int hora = cal.get(Calendar.HOUR_OF_DAY);

    int diaSemana = cal.get(Calendar.DAY_OF_WEEK);

    static String nombreProfesores[];

    private String[] semana = new String[] { "", "Domingo ", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado" };

    /** Creates new form Consultas_Maestro */
    public DondeEsta() {
        nombreProfesores = new String[Conexion.profesoresRegistrados()];
        nombreProfesores = Conexion.llenarNombreProfesores();
        initComponents();
        this.setLocationRelativeTo(null);
        this.setIconImage(new ImageIcon(getClass().getResource("/imagenes/icono3.png")).getImage());
        setLocacion();
        Timer lol = new Timer();
        timerIntervalo(lol);
        System.out.println("hora!! " + hora);
    }

    void imprime(String texto) {
        imprime("", texto);
    }

    void imprime(String texto, String texto2) {
        antes.setText(texto);
        donde.setText(texto2);
    }

    private void setLocacion() {
        all: {
            String profe = profeBox.getSelectedItem().toString();
            if (Conexion.existeActividadProfesor(profe, diaActual, mesActual, añoActual)) {
                ActividadComplementaria act[] = new ActividadComplementaria[Conexion.acts(profe)];
                act = Conexion.llenarActs(profe);
                for (int lol = 0; lol < act.length; lol++) {
                    if (act[lol].getFechaDia() == diaActual && Integer.parseInt(act[lol].getFechaMes()) == mesActual && act[lol].getFechaAño() == añoActual) {
                        imprime("El profesor se encuentra en una actividad complementaria", act[lol].getDescripcion());
                        break all;
                    }
                }
            } else if (Conexion.existeFaltaProfesor(profe, diaActual, mesActual, añoActual)) {
                Falta faltas[] = new Falta[Conexion.faltas(profe)];
                faltas = Conexion.llenarFaltasProfe(profe);
                for (int lol = 0; lol < faltas.length; lol++) {
                    if (faltas[lol].getFechaDia() == diaActual && Integer.parseInt(faltas[lol].getFehcames()) == mesActual && faltas[lol].getFechaAno() == añoActual) {
                        imprime("El profesor tiene una falta este dia", faltas[lol].getRazonFalta());
                        break all;
                    }
                }
            } else if (Conexion.existeHorario(profe)) {
                int horario = Conexion.horas(profe, semana[diaSemana], "Todos");
                if (horario > 0) {
                    Horario horas[] = new Horario[horario];
                    horas = Conexion.llenarHorarioProfe(profe, semana[diaSemana], "Todos");
                    Horas: {
                        for (int j = 0; j < horas.length; j++) {
                            if (horas[j].getHora() == hora) {
                                imprime("El profesor esta en clase con el grupo " + horas[j].getGrupo() + " en el salón " + horas[j].getSalon());
                                if (Conexion.existeFaltaHora(profe, diaActual, mesActual, añoActual, horas[j].getHora(), Integer.parseInt(horas[j].getHoraTermino()))) {
                                    FaltaHora faltasH[] = new FaltaHora[Conexion.faltasPorHora(profe)];
                                    faltasH = Conexion.llenarFaltasHora(profe);
                                    for (int lol = 0; lol < faltasH.length; lol++) {
                                        if (faltasH[lol].getFechaDia() == diaActual && Integer.parseInt(faltasH[lol].getFehcames()) == mesActual && faltasH[lol].getFechaAno() == añoActual) {
                                            imprime("El profesor no se presento a impartir clase por " + faltasH[lol].getRazonFalta());
                                            break all;
                                        }
                                    }
                                    break Horas;
                                }
                            }
                        }
                        for (int i = 0; i < horas.length - 1; i++) {
                            for (int j = 1; j < horas.length; j++) {
                                if (horas[i].getHora() < horas[j].getHora()) {
                                    Horario aux = horas[j];
                                    horas[j] = horas[i];
                                    horas[i] = aux;
                                }
                            }
                        }
                        Horario antes = new Horario();
                        Horario despues = new Horario();
                        HorasElegidas: {
                            for (int i = 0; i < horas.length; i++) {
                                if (horas[i].getHora() > hora) {
                                } else {
                                    antes = horas[i];
                                    despues = horas[i];
                                    if (i != 0) {
                                        despues = horas[i - 1];
                                    }
                                    break HorasElegidas;
                                }
                            }
                        }
                        if (antes.getHoraComienzo().compareToIgnoreCase(despues.getHoraComienzo()) == 0) {
                            imprime("La clase del profesor comenzo a las " + antes.getHoraComienzo() + " y termina a las " + antes.getHoraTermino());
                        } else {
                            imprime("La clase del profesor comenzo a las " + antes.getHoraComienzo() + " y termina a las " + antes.getHoraTermino(), "la próxima clase comenzará a las " + despues.getHoraComienzo());
                        }
                    }
                } else {
                    imprime("El profesor no tiene asignadas horas en este día");
                }
            } else {
                imprime("No hay actividades para este profesor");
            }
        }
    }

    public void timerIntervalo(Timer t) {
        Date actual = new Date();
        int horas = actual.getHours();
        int minutos = actual.getMinutes();
        int segundos = actual.getSeconds();
        int hora;
        String tiempo = "";
        if (horas > 12) {
            hora = horas - 12;
        } else {
            hora = horas;
        }
        if (hora < 10) {
            tiempo += "0";
        }
        tiempo += hora;
        tiempo += ":";
        if (minutos < 10) {
            tiempo += "0";
        }
        tiempo += minutos + ":";
        if (segundos < 10) {
            tiempo += "0";
        }
        tiempo += segundos;
        if (horas > 12) {
            tiempo += " pm";
        } else {
            tiempo += " am";
        }
        horaActual.setText(tiempo);
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        profeBox = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        donde = new javax.swing.JLabel();
        antes = new javax.swing.JLabel();
        horaActual = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Donde esta el profesor?");
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);
        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18));
        jLabel1.setText("Dónde esta el profesor?");
        jButton2.setText("Salir");
        jButton2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        profeBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        profeBox.setModel(new javax.swing.DefaultComboBoxModel(nombreProfesores));
        profeBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profeBoxActionPerformed(evt);
            }
        });
        jLabel2.setText("Profesor:");
        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        donde.setText("El profesor no tiene ninguna actividad asignada para esta hora");
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGap(19, 19, 19).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(antes, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE).addComponent(donde, javax.swing.GroupLayout.Alignment.LEADING)).addContainerGap(20, Short.MAX_VALUE)));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGap(42, 42, 42).addComponent(antes, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(donde).addContainerGap(97, Short.MAX_VALUE)));
        horaActual.setText("jLabel4");
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGap(46, 46, 46).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel2).addGroup(jPanel1Layout.createSequentialGroup().addGap(99, 99, 99).addComponent(profeBox, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)))).addGroup(jPanel1Layout.createSequentialGroup().addGap(75, 75, 75).addComponent(jLabel1)).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(horaActual).addGap(112, 112, 112).addComponent(jButton2)).addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(11, 11, 11).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(profeBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel2)).addGap(18, 18, 18).addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGap(28, 28, 28).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jButton2).addComponent(horaActual)).addContainerGap()));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        pack();
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }

    private void profeBoxActionPerformed(java.awt.event.ActionEvent evt) {
        setLocacion();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new ConsultaRetardoProfesor().setVisible(true);
            }
        });
    }

    private javax.swing.JLabel antes;

    private javax.swing.JLabel donde;

    private javax.swing.JLabel horaActual;

    private javax.swing.JButton jButton2;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JComboBox profeBox;
}
