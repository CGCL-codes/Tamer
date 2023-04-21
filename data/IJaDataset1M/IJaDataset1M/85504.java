package com.simplebundle.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

public class MapPanel extends javax.swing.JPanel {

    private JFileChooser fileChooser;

    private BufferedImage image;

    public MapPanel() {
        fileChooser = new JFileChooser();
        this.setName("Teste");
        initComponents();
        this.mainButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                buscar();
            }
        });
        this.salvarButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                salvar();
            }
        });
    }

    public void salvar() {
        int retorno = fileChooser.showSaveDialog(this);
        if (retorno == JFileChooser.APPROVE_OPTION) {
            try {
                ImageIO.write(image, "gif", fileChooser.getSelectedFile());
            } catch (IOException ex) {
                Logger.getLogger(MapPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void buscar() {
        String origem = origemTextField.getText();
        String destino = destinoTextField.getText();
        if (!isValidEndereco(origem)) {
            origem = "r. osvaldo glatz 40, jaragua do sul";
            origemTextField.setText(origem);
        }
        if (!isValidEndereco(destino)) {
            destino = null;
            destinoTextField.setText("");
        }
        String retornoOrigem = null;
        if (origem != null) retornoOrigem = geocode(origem);
        String retornoDestino = null;
        if (destino != null) retornoDestino = geocode(destino);
        String latitudeOrigem = null;
        String longitudeOrigem = null;
        if (retornoOrigem != null) {
            String geocodeInfoOrigem[] = retornoOrigem.split(",");
            latitudeOrigem = geocodeInfoOrigem[2];
            longitudeOrigem = geocodeInfoOrigem[3];
        }
        String latitudeDestino = null;
        String longitudeDestino = null;
        if (retornoDestino != null) {
            String geocodeInfoDestino[] = retornoDestino.split(",");
            latitudeDestino = geocodeInfoDestino[2];
            longitudeDestino = geocodeInfoDestino[3];
        }
        if (latitudeOrigem != null && longitudeOrigem != null) {
            if (latitudeDestino != null && longitudeDestino != null) {
                image = map(latitudeOrigem, longitudeOrigem, latitudeDestino, longitudeDestino, 640, 512, 15);
            } else image = map(latitudeOrigem, longitudeOrigem, null, null, 640, 512, 15);
            mainImagePanel.setIcon(new ImageIcon(image));
        }
        mainImagePanel.setSize(640, 512);
        mainImagePanel.setPreferredSize(new Dimension(640, 512));
        mainImagePanel.updateUI();
        jScrollPane1.updateUI();
        this.updateUI();
    }

    private boolean isValidEndereco(String endereco) {
        return (endereco != null && endereco.trim().length() > 0);
    }

    private String geocode(String location) {
        try {
            String data = "http://maps.google.com/maps/geo?output=csv&key=ABQIAAAAlrRB8uy8uKPmv6-ksEcoyBTh2_HzqcFG_4G17_9mYSXtFUbfuBSTZ-vI0VLq7e9SozOvXkSF47Lp9g&q=" + URLEncoder.encode(location, "UTF-8") + "";
            URL url = new URL(data);
            return new String(grabData(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private BufferedImage map(String latitudeOrigem, String longitudeOrigem, String latitudeDestino, String longitudeDestino, int width, int height, int zoom) {
        BufferedImage image = null;
        try {
            String data = "zoom=" + zoomSlider.getValue() + "&size=" + width + "x" + height + "&markers=" + latitudeOrigem + "," + longitudeOrigem + ",blues" + getOutroMarcador(latitudeDestino, longitudeDestino) + getPath(latitudeOrigem, longitudeOrigem, latitudeDestino, longitudeDestino) + "&key=ABQIAAAAlrRB8uy8uKPmv6-ksEcoyBTh2_HzqcFG_4G17_9mYSXtFUbfuBSTZ-vI0VLq7e9SozOvXkSF47Lp9g";
            URL url = new URL("http://maps.google.com/staticmap?" + data);
            byte[] imageData = grabData(url);
            ByteArrayInputStream imageIn = new ByteArrayInputStream(imageData);
            image = ImageIO.read(imageIn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    private String getPath(String latitudeOrigem, String longitudeOrigem, String latitudeDestino, String longitudeDestino) {
        String retorno = "";
        if (latitudeDestino != null && longitudeDestino != null) retorno = "&path=rgb:0x0000ff,weight:5|" + latitudeOrigem + "," + longitudeOrigem + "|" + latitudeDestino + "," + longitudeDestino;
        return retorno;
    }

    private String getCenter(String latitudeOrigem, String longitudeOrigem) {
        String retorno = "";
        retorno = "center=" + latitudeOrigem + "," + longitudeOrigem + "&";
        return retorno;
    }

    private String getOutroMarcador(String latitudeDestino, String longitudeDestino) {
        String retorno = "";
        if (latitudeDestino != null && longitudeDestino != null) retorno = "|" + latitudeDestino + "," + longitudeDestino + ",red";
        return retorno;
    }

    private byte[] grabData(URL url) throws Exception {
        URLConnection conn = url.openConnection();
        InputStream in = conn.getInputStream();
        byte[] buffer = new byte[1000];
        int readed = -1;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while ((readed = in.read(buffer)) != -1) {
            out.write(buffer, 0, readed);
        }
        return out.toByteArray();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        origemTextField = new javax.swing.JTextField();
        mainButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        destinoTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        mainImagePanel = new javax.swing.JLabel();
        salvarButton = new javax.swing.JButton();
        zoomSlider = new javax.swing.JSlider();
        jLabel3 = new javax.swing.JLabel();
        jLabel1.setText("Origem:");
        mainButton.setText("buscar");
        jLabel2.setText("Destino:");
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setMaximumSize(new java.awt.Dimension(665, 32767));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(665, 23));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(665, 100));
        jScrollPane1.setViewportView(mainImagePanel);
        salvarButton.setText("salvar");
        zoomSlider.setMaximum(19);
        zoomSlider.setPaintLabels(true);
        zoomSlider.setPaintTicks(true);
        zoomSlider.setSnapToTicks(true);
        zoomSlider.setValue(14);
        jLabel3.setText("Zoom:");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel2).addComponent(jLabel1).addComponent(jLabel3)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(layout.createSequentialGroup().addComponent(zoomSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(mainButton)).addComponent(destinoTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE).addComponent(origemTextField))).addGroup(layout.createSequentialGroup().addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 665, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(salvarButton))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(origemTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(destinoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel3).addComponent(mainButton).addComponent(zoomSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(salvarButton).addContainerGap()).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE))));
    }

    private javax.swing.JTextField destinoTextField;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JButton mainButton;

    private javax.swing.JLabel mainImagePanel;

    private javax.swing.JTextField origemTextField;

    private javax.swing.JButton salvarButton;

    private javax.swing.JSlider zoomSlider;
}
