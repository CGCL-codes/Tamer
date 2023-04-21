package gui;

import hqmanager.Colecao;
import hqmanager.Historia;
import hqmanager.Manager;
import hqmanager.Revista;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Roberto
 */
public class ExibeRevista extends javax.swing.JFrame {

    private Colecao colecao;

    private Manager manager;

    private Revista revista;

    private Historia historia;

    private Mensagem aviso;

    /** Creates new form ExibeRevista */
    public ExibeRevista(Manager manager, Colecao colecao, Revista revista) {
        initComponents();
        jPanel2.setVisible(false);
        this.manager = manager;
        this.colecao = colecao;
        this.revista = revista;
        atualiza();
    }

    public void atualiza() {
        jTextField1.setText(revista.getTitulo());
        jTextField2.setText(revista.getNumero() + "");
        jTextField3.setText(revista.getIsbn());
        jTextAreaSinopse.setText(revista.getSinopse());
        jListHistorias.setListData(new Vector(revista.getHistorias()));
        if (aviso == null) {
            if (jTextField2.getText().equals("0") && !jTextField3.getText().equals("")) aviso = new Mensagem("Complete os campos que faltam!", "Campo: Número", "Revista - Complete os dados"); else if (!jTextField2.getText().equals("0") && jTextField3.getText().equals("")) aviso = new Mensagem("Complete os campos que faltam!", "Campo: ISBN", "Revista - Complete os dados"); else if (jTextField2.getText().equals("0") && jTextField3.getText().equals("")) aviso = new Mensagem("Complete os campos que faltam!", "Campos: Número e ISBN", "Revista - Complete os dados");
            aviso.show();
        }
    }

    public void salva() {
        this.revista.setTitulo(jTextField1.getText());
        this.revista.setNumero(Integer.parseInt(jTextField2.getText()));
        this.revista.setIsbn(jTextField3.getText());
        this.revista.setSinopse(jTextAreaSinopse.getText());
        this.manager.setColecao(colecao);
        this.manager.setRevista(revista);
        atualiza();
    }

    private ExibeRevista() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jScrollPaneSinopse = new javax.swing.JScrollPane();
        jTextAreaSinopse = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListHistorias = new javax.swing.JList();
        jPanel2 = new javax.swing.JPanel();
        jTextFieldAdicionar = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jButton8 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        barraStatus = new javax.swing.JLabel();
        setTitle("HQ-Manager - Revista");
        jTextAreaSinopse.setBackground(new java.awt.Color(240, 240, 240));
        jTextAreaSinopse.setColumns(20);
        jTextAreaSinopse.setEditable(false);
        jTextAreaSinopse.setRows(5);
        jTextAreaSinopse.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextAreaSinopseMouseClicked(evt);
            }
        });
        jTextAreaSinopse.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextAreaSinopseFocusLost(evt);
            }
        });
        jTextAreaSinopse.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextAreaSinopseKeyPressed(evt);
            }

            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextAreaSinopseKeyReleased(evt);
            }
        });
        jScrollPaneSinopse.setViewportView(jTextAreaSinopse);
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Historias"));
        jListHistorias.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListHistoriasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jListHistorias);
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Nova História"));
        jTextFieldAdicionar.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldAdicionarKeyPressed(evt);
            }
        });
        jLabel6.setText("Título: ");
        jButton4.setText("OK");
        jButton4.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jButton5.setText("Cancelar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addComponent(jLabel6).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextFieldAdicionar, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)).addGroup(jPanel2Layout.createSequentialGroup().addGap(28, 28, 28).addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE).addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel6).addComponent(jTextFieldAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButton5).addComponent(jButton4)).addContainerGap()));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/img_Add.png")));
        jButton2.setText("Adicionar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/img_Modify.png")));
        jButton1.setText("Editar");
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/img_Delete.png")));
        jButton3.setText("Remover");
        jButton3.setEnabled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jButton6.setText("Fechar");
        jButton6.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jButton7.setText("Salvar");
        jButton7.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup().addGap(20, 20, 20).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jButton6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE).addComponent(jButton7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup().addGap(1, 1, 1).addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE))).addGap(22, 22, 22)));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addComponent(jButton2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(17, 17, 17)));
        jButton8.setForeground(new java.awt.Color(0, 51, 255));
        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/ajuda.PNG")));
        jButton8.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup().addContainerGap(175, Short.MAX_VALUE).addComponent(jButton8)));
        jPanel5Layout.setVerticalGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel5Layout.createSequentialGroup().addComponent(jButton8).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(13, 13, 13).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)).addContainerGap()));
        jLabel2.setText("Número");
        jTextField2.setEditable(false);
        jTextField2.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField2MouseClicked(evt);
            }
        });
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField2KeyPressed(evt);
            }
        });
        jLabel3.setText("ISBN");
        jTextField3.setEditable(false);
        jTextField3.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField3MouseClicked(evt);
            }
        });
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField3KeyPressed(evt);
            }
        });
        jLabel1.setText("Nome");
        jTextField1.setEditable(false);
        jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField1MouseClicked(evt);
            }
        });
        jTextField1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });
        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(jPanel4Layout.createSequentialGroup().addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(37, 37, 37).addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(jPanel4Layout.createSequentialGroup().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel3).addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        barraStatus.setFont(new java.awt.Font("Tahoma", 1, 14));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(14, 14, 14)).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(barraStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addComponent(jScrollPaneSinopse, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE).addContainerGap()).addGroup(layout.createSequentialGroup().addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE).addContainerGap())))));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(jScrollPaneSinopse, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(barraStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(21, 21, 21)));
        pack();
    }

    private void jTextAreaSinopseFocusLost(java.awt.event.FocusEvent evt) {
    }

    private void jTextAreaSinopseKeyPressed(java.awt.event.KeyEvent evt) {
    }

    private void jTextFieldAdicionarKeyPressed(java.awt.event.KeyEvent evt) {
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        atualizaStatus("Digite o nome da nova revista e clique em OK, ou pressione ENTER.");
        jTextFieldAdicionar.setText("");
        jPanel2.setVisible(true);
        jButton2.setEnabled(false);
        jButton1.setEnabled(false);
        jButton3.setEnabled(false);
        jButton6.setEnabled(false);
        jTextFieldAdicionar.requestFocus(true);
        jListHistorias.setEnabled(false);
    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
        Mensagem mensagem = null;
        boolean incluir = true;
        for (Historia hist : revista.getHistorias()) {
            if (hist.getTitulo().equals(jTextFieldAdicionar.getText().toString())) {
                mensagem = new Mensagem("Inclusão não realizada! História já existente!", "Digite um nome diferente de " + jTextFieldAdicionar.getText().toString(), "Erro ao Incluir Coleção");
                incluir = false;
            }
        }
        if (!incluir) {
            jTextFieldAdicionar.requestFocus();
            jTextFieldAdicionar.selectAll();
            mensagem.show();
        } else if (incluir) {
            mensagem = new Mensagem("Inclusão processada com sucesso!", "História: " + jTextFieldAdicionar.getText(), "Inclusão de História");
            adiciona();
            atualizaStatus("");
            jListHistorias.setEnabled(true);
            mensagem.show();
        }
    }

    private void jListHistoriasMouseClicked(java.awt.event.MouseEvent evt) {
        if (jListHistorias.getSelectedIndex() != -1) {
            jButton1.setEnabled(true);
            jButton3.setEnabled(true);
        } else {
            jButton1.setEnabled(false);
            jButton3.setEnabled(false);
        }
        if (evt.getClickCount() == 2) {
            ExibeHistoria exibeHistoria = new ExibeHistoria(manager, colecao, revista, (Historia) jListHistorias.getSelectedValue());
            exibeHistoria.show();
        }
    }

    private void jTextField1MouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            atualizaStatus("Digite o novo nome e pressione ENTER!");
            jTextField1.setEditable(true);
            jTextField1.requestFocus();
            jTextField1.select(0, 255);
            salva();
        }
    }

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == 10) {
            salva();
            jTextField1.setEditable(false);
        }
    }

    private void jTextField2KeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == 10) {
            salva();
            jTextField2.setEditable(false);
        }
    }

    private void jTextField3KeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == 10) {
            salva();
            jTextField3.setEditable(false);
        }
    }

    private void jTextField2MouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            jTextField2.setEditable(true);
            jTextField2.requestFocus();
            jTextField2.select(0, 255);
        }
    }

    private void jTextField3MouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            jTextField3.setEditable(true);
            jTextField3.requestFocus();
            jTextField3.select(0, 255);
        }
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        if (jListHistorias.getSelectedValue() != null) {
            ExibeHistoria exibe = new ExibeHistoria(manager, colecao, revista, (Historia) jListHistorias.getSelectedValue());
            exibe.show();
        }
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        Historia remocao = new Historia("");
        remocao = (Historia) jListHistorias.getSelectedValue();
        Mensagem mensagem = new Mensagem("História removida com sucesso!", "História: " + remocao.getTitulo(), "Remoçao de Revista");
        revista.removeHistoria(remocao);
        this.manager.setColecao(colecao);
        this.manager.setRevista(revista);
        atualiza();
        mensagem.show();
    }

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {
        salva();
        this.dispose();
    }

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {
        jListHistorias.setEnabled(true);
        jPanel2.setVisible(false);
        jButton2.setEnabled(true);
        jButton1.setEnabled(false);
        jButton3.setEnabled(false);
        jButton6.setEnabled(true);
        atualizaStatus("");
    }

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {
        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
        try {
            desktop.open(new File("HQ-Ajuda.htm"));
        } catch (IOException ex) {
            Logger.getLogger(ExibeManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void jTextAreaSinopseMouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            jTextAreaSinopse.setEditable(true);
            atualizaStatus("Insira os comentários e pressione CTRL + ENTER");
        }
    }

    private void jTextAreaSinopseKeyReleased(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == 10 && evt.isControlDown()) {
            atualizaStatus("");
            jTextAreaSinopse.setEditable(false);
            adiciona();
            salva();
        }
    }

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {
        salva();
    }

    private void adiciona() {
        revista.addHistoria(new Historia(jTextFieldAdicionar.getText()));
        this.manager.setColecao(colecao);
        this.manager.setRevista(revista);
        jPanel2.setVisible(false);
        jButton2.setEnabled(true);
        jButton1.setEnabled(false);
        jButton3.setEnabled(false);
        jButton6.setEnabled(true);
        atualiza();
    }

    private void atualizaStatus(String x) {
        if (x.equals("")) barraStatus.setText("Clique sobre uma das Revistas para editar ou removê-las."); else barraStatus.setText(x);
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new ExibeRevista().setVisible(true);
            }
        });
    }

    private javax.swing.JLabel barraStatus;

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;

    private javax.swing.JButton jButton3;

    private javax.swing.JButton jButton4;

    private javax.swing.JButton jButton5;

    private javax.swing.JButton jButton6;

    private javax.swing.JButton jButton7;

    private javax.swing.JButton jButton8;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JList jListHistorias;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JPanel jPanel5;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPaneSinopse;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JTextArea jTextAreaSinopse;

    private javax.swing.JTextField jTextField1;

    private javax.swing.JTextField jTextField2;

    private javax.swing.JTextField jTextField3;

    private javax.swing.JTextField jTextFieldAdicionar;
}
