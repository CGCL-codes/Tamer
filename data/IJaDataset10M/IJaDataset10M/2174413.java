package GabrielPIMP_1examenParcial;

import java.awt.Frame;
import javax.swing.JOptionPane;

/**
 *
 * @author  Gabriel Miranda
 */
public class OtraBoleada extends javax.swing.JFrame {

    private char[] abcedario = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', '�', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

    private int[] valores = { 5, 1, 2, 5, 4, 7, 8, 9, 6, 5, 1, 2, 3, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 1, 2, 0, 3 };

    public static int ValorMagico = 0;

    public OtraBoleada() {
        initComponents();
        lblNumero.setText(Integer.toString(ValorMagico));
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        nombre = new javax.swing.JFormattedTextField();
        repetir = new javax.swing.JButton();
        usadas = new javax.swing.JLabel();
        valoresLetras = new javax.swing.JLabel();
        lblMagico = new javax.swing.JLabel();
        lblNumero = new javax.swing.JLabel();
        lblResultado = new javax.swing.JLabel();
        lblSuma = new javax.swing.JLabel();
        getContentPane().setLayout(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Maquina Boleada");
        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText("nombre");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(20, 30, 80, 30);
        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("Letras Usadas");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(20, 80, 100, 30);
        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText("Sus Valores");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(20, 130, 90, 30);
        nombre.setForeground(new java.awt.Color(0, 51, 204));
        nombre.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        nombre.setFont(new java.awt.Font("Tahoma", 1, 14));
        nombre.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                nombreKeyPressed(evt);
            }
        });
        getContentPane().add(nombre);
        nombre.setBounds(150, 30, 260, 30);
        repetir.setFont(new java.awt.Font("Tahoma", 0, 14));
        repetir.setText("Repetir");
        repetir.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                repetirMouseClicked(evt);
            }
        });
        getContentPane().add(repetir);
        repetir.setBounds(170, 240, 110, 40);
        usadas.setFont(new java.awt.Font("Tahoma", 0, 14));
        usadas.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        getContentPane().add(usadas);
        usadas.setBounds(150, 80, 260, 30);
        valoresLetras.setFont(new java.awt.Font("Tahoma", 0, 14));
        valoresLetras.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        getContentPane().add(valoresLetras);
        valoresLetras.setBounds(150, 120, 260, 30);
        lblMagico.setFont(new java.awt.Font("Tahoma", 0, 14));
        lblMagico.setText("Numero Magico");
        getContentPane().add(lblMagico);
        lblMagico.setBounds(20, 180, 100, 40);
        lblNumero.setFont(new java.awt.Font("Tahoma", 1, 18));
        lblNumero.setForeground(new java.awt.Color(204, 0, 0));
        lblNumero.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNumero.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(lblNumero);
        lblNumero.setBounds(150, 180, 60, 40);
        lblResultado.setFont(new java.awt.Font("Tahoma", 0, 14));
        lblResultado.setText("Resultado");
        getContentPane().add(lblResultado);
        lblResultado.setBounds(250, 180, 80, 40);
        lblSuma.setFont(new java.awt.Font("Tahoma", 1, 18));
        lblSuma.setForeground(new java.awt.Color(204, 0, 0));
        lblSuma.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSuma.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(lblSuma);
        lblSuma.setBounds(350, 180, 60, 40);
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 457) / 2, (screenSize.height - 342) / 2, 457, 342);
    }

    private void repetirMouseClicked(java.awt.event.MouseEvent evt) {
        cleanGame();
        this.setVisible(false);
        Frame MyFrame = new Frame();
        Configuracion inicio = new Configuracion(MyFrame, true);
        inicio.setVisible(true);
    }

    private void cleanGame() {
        ValorMagico = 0;
        nombre.setText("");
        usadas.setText("");
        valoresLetras.setText("");
        lblNumero.setText("");
        lblSuma.setText("");
    }

    private void nombreKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == 10) {
            String juntar = "", susvalores = "";
            char[] letras = nombre.getText().trim().toCharArray();
            String usando = juntarLetras(letras);
            char[] conjunto = usando.toCharArray();
            for (int a = 0; a < conjunto.length; a++) {
                for (int b = 0; b < abcedario.length; b++) {
                    if (conjunto[a] == abcedario[b]) {
                        susvalores = susvalores + valores[b];
                    }
                }
            }
            char[] conjuntonumeros = susvalores.toCharArray();
            int suma = 0;
            for (int a = 0; a < conjuntonumeros.length; a++) {
                suma = suma + Integer.parseInt(String.valueOf(conjuntonumeros[a]));
            }
            usadas.setText(usando);
            valoresLetras.setText(susvalores);
            lblSuma.setText(Integer.toString(suma));
            if (suma == ValorMagico) {
                JOptionPane.showMessageDialog(null, " Tu nombre puede participar ...");
            } else {
                JOptionPane.showMessageDialog(null, " Tu nombre no es participable, intenta otro diferente...");
            }
        }
    }

    public String juntarLetras(char[] letras) {
        String juntar = "";
        for (int a = 0; a < 20; a++) {
            if (a == letras.length) {
                a = 0;
            }
            if (juntar.length() < 20) {
                juntar = juntar + letras[a];
            } else {
                return juntar;
            }
        }
        return juntar;
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new OtraBoleada().setVisible(true);
            }
        });
    }

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel lblMagico;

    private javax.swing.JLabel lblNumero;

    private javax.swing.JLabel lblResultado;

    private javax.swing.JLabel lblSuma;

    private javax.swing.JFormattedTextField nombre;

    private javax.swing.JButton repetir;

    private javax.swing.JLabel usadas;

    private javax.swing.JLabel valoresLetras;
}
