package vq.codec.ui;

import java.util.List;
import javax.swing.JOptionPane;
import vq.codec.entities.Dat;
import vq.codec.entities.Dictionary;
import vq.codec.exception.DecodingException;
import vq.codec.exception.SystemException;
import vq.codec.managers.VQCodecManager;
import vq.codec.managers.VQFileManager;

/**
 * Classe criada para representar o frame principal de entrada de dados para a decodificação
 * 
 * @author Gutemberg Rodrigues
 * @since Outubro de 2009
 * @version 0.2
 */
public class ScreenDecode extends javax.swing.JFrame {

    private VQFileManager fileManager;

    private VQCodecManager codedManager;

    public ScreenDecode() {
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        fileManager = VQFileManager.getInstance();
        codedManager = VQCodecManager.getInstance();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        labelDictionary = new javax.swing.JLabel();
        labelSource = new javax.swing.JLabel();
        txtDictionary = new javax.swing.JTextField();
        txtSource = new javax.swing.JTextField();
        labelInformation = new javax.swing.JLabel();
        buttonDecode = new javax.swing.JButton();
        buttonClean = new javax.swing.JButton();
        labelPastaDestino = new javax.swing.JLabel();
        txtPastaDestino = new javax.swing.JTextField();
        labelInformation1 = new javax.swing.JLabel();
        labelInformation2 = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        labelDictionary.setText("Informe o caminho do arquivo do dicionário:");
        labelSource.setText("Informe o caminho do arquivo a ser decodificado:");
        txtDictionary.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDictionaryActionPerformed(evt);
            }
        });
        txtSource.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSourceActionPerformed(evt);
            }
        });
        labelInformation.setForeground(new java.awt.Color(153, 153, 153));
        labelInformation.setText("exemplo:  C:\\Users\\Administrador\\Desktop\\dicionarios\\nomedicionario.dic");
        buttonDecode.setFont(new java.awt.Font("Tahoma", 1, 11));
        buttonDecode.setIcon(new javax.swing.ImageIcon("./images/iconeConfirmar.gif"));
        buttonDecode.setText("Decodificar");
        buttonDecode.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDecodeActionPerformed(evt);
            }
        });
        buttonClean.setIcon(new javax.swing.ImageIcon("./images/imagemLimpar.jpg"));
        buttonClean.setText("Limpar Campos");
        buttonClean.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCleanActionPerformed(evt);
            }
        });
        labelPastaDestino.setText("Informe a pasta de destino do arquivo:");
        txtPastaDestino.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPastaDestinoActionPerformed(evt);
            }
        });
        labelInformation1.setForeground(new java.awt.Color(153, 153, 153));
        labelInformation1.setText("exemplo: C:\\Users\\Administrador\\Desktop\\sinais\\nomeArquivo");
        labelInformation2.setForeground(new java.awt.Color(153, 153, 153));
        labelInformation2.setText("exemplo: C:\\Users\\Administrador\\Desktop\\nomeArquivo.vq");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(labelDictionary).addGap(440, 440, 440)).addGroup(layout.createSequentialGroup().addComponent(txtDictionary, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE).addContainerGap()).addGroup(layout.createSequentialGroup().addComponent(labelInformation).addContainerGap(299, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addComponent(labelSource).addContainerGap(443, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addComponent(txtSource, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE).addContainerGap()).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(labelInformation2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 381, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addComponent(labelInformation1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 352, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addComponent(labelPastaDestino, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE).addGap(391, 391, 391)).addComponent(txtPastaDestino, javax.swing.GroupLayout.DEFAULT_SIZE, 650, Short.MAX_VALUE)).addContainerGap()).addGroup(layout.createSequentialGroup().addComponent(buttonClean).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 365, Short.MAX_VALUE).addComponent(buttonDecode, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()))));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(labelDictionary).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(txtDictionary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(labelInformation).addGap(18, 18, 18).addComponent(labelSource).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(txtSource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(labelInformation1).addGap(18, 18, 18).addComponent(labelPastaDestino).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(txtPastaDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(labelInformation2).addGap(37, 37, 37).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(buttonDecode, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(buttonClean, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(21, Short.MAX_VALUE)));
        pack();
    }

    private void txtDictionaryActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void txtSourceActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void buttonDecodeActionPerformed(java.awt.event.ActionEvent evt) {
        checkClean();
        Dictionary dictionary = null;
        List<Integer> encoded = null;
        Dat decoded = null;
        try {
            dictionary = fileManager.loadDictionaryFile(txtDictionary.getText());
            encoded = fileManager.loadEncodedFile(txtSource.getText());
            decoded = codedManager.decode(encoded, dictionary);
            fileManager.generateDatFile(decoded, txtPastaDestino.getText());
            fileManager.generatePgmFile(decoded, txtPastaDestino.getText());
            JOptionPane.showMessageDialog(this, "Processo Finalizado!", "Fim", JOptionPane.INFORMATION_MESSAGE);
        } catch (DecodingException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (SystemException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void checkClean() {
        if (txtDictionary.getText().length() == 0 || txtSource.getText().length() == 0 || txtPastaDestino.getText().length() == 0) {
            JOptionPane.showMessageDialog(this, "Campo não informado", "Atenção", JOptionPane.ERROR_MESSAGE);
            throw new SystemException("Campo da Tela em Branco!", null);
        }
    }

    private void buttonCleanActionPerformed(java.awt.event.ActionEvent evt) {
        txtDictionary.setText("");
        txtSource.setText("");
        txtPastaDestino.setText("");
        txtDictionary.requestFocus();
    }

    private void txtPastaDestinoActionPerformed(java.awt.event.ActionEvent evt) {
    }

    public void openFrameScreenDecode() {
        ScreenDecode frameScreenDecode = new ScreenDecode();
        frameScreenDecode.setVisible(true);
        frameScreenDecode.setLocationRelativeTo(null);
    }

    private javax.swing.JButton buttonClean;

    private javax.swing.JButton buttonDecode;

    private javax.swing.JLabel labelDictionary;

    private javax.swing.JLabel labelInformation;

    private javax.swing.JLabel labelInformation1;

    private javax.swing.JLabel labelInformation2;

    private javax.swing.JLabel labelPastaDestino;

    private javax.swing.JLabel labelSource;

    private javax.swing.JTextField txtDictionary;

    private javax.swing.JTextField txtPastaDestino;

    private javax.swing.JTextField txtSource;
}
