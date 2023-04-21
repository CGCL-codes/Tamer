package camadaSO;

public class MainServer {

    public static void main(String[] args) {
        final javax.swing.JFrame telaServ;
        telaServ = new javax.swing.JFrame("Servidor");
        telaServ.setLayout(null);
        telaServ.setSize(280, 100);
        telaServ.setDefaultCloseOperation(javax.swing.JFrame.DO_NOTHING_ON_CLOSE);
        telaServ.setVisible(true);
        javax.swing.JButton botaoIniciar = new javax.swing.JButton("Iniciar Servidor");
        botaoIniciar.setBounds(10, 5, 200, 20);
        telaServ.add(botaoIniciar);
        javax.swing.JButton botaoFim = new javax.swing.JButton("Finalizar Servidor");
        botaoFim.setBounds(10, 35, 200, 20);
        telaServ.add(botaoFim);
        botaoIniciar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (!camadaRN.RNEliteSquadServer.getRNServer().iniciarServidor()) {
                    javax.swing.JOptionPane.showMessageDialog(null, "Nao foi possivel iniciar");
                    telaServ.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
                }
                javax.swing.JOptionPane.showMessageDialog(null, "Servidor iniciado");
            }
        });
        botaoFim.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                camadaRN.RNEliteSquadServer.getRNServer().finalizarServidor();
                javax.swing.JOptionPane.showMessageDialog(null, "Servidor finalizado");
                telaServ.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
            }
        });
    }
}
