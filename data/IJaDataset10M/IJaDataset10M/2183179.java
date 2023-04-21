package com.hadeslee.yoyoplayer.lyric;

import com.hadeslee.yoyoplayer.playlist.PlayListItem;
import com.hadeslee.yoyoplayer.util.Config;
import com.hadeslee.yoyoplayer.util.Util;
import java.io.File;
import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author  Admin
 */
public class WebSearchDialog extends javax.swing.JDialog {

    private static Logger log = Logger.getLogger(WebSearchDialog.class.getName());

    private PlayListItem item;

    private List<SearchResult> list = new ArrayList<SearchResult>();

    private TableRowSorter<TableModel> sorter;

    private LyricPanel lp;

    /** Creates new form WebSearchDialog */
    private WebSearchDialog(JDialog parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initTable();
    }

    public WebSearchDialog(PlayListItem item, LyricPanel lp) {
        this(Config.getConfig().getLrcWindow(), true);
        this.item = item;
        this.lp = lp;
        initOther();
        this.setLocationRelativeTo(Config.getConfig().getLrcWindow());
    }

    private void initTable() {
        TableColumnModel model = new DefaultTableColumnModel();
        TableColumn c1 = new TableColumn(0, 100);
        TableColumn c2 = new TableColumn(1, 100);
        c1.setHeaderValue(Config.getResource("WebSearchDialog.artist"));
        c2.setHeaderValue(Config.getResource("WebSearchDialog.title"));
        model.addColumn(c1);
        model.addColumn(c2);
        MyTableModel tm = new MyTableModel();
        sorter = new TableRowSorter<TableModel>(tm);
        table.setModel(tm);
        table.setColumnModel(model);
        table.setRowSorter(sorter);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void initOther() {
        artistJT.setText(item.getArtist());
        titleJT.setText(item.getTitle());
        fileName.setText(item.getFormattedName() + ".lrc");
    }

    private class MyTableModel extends AbstractTableModel {

        public int getRowCount() {
            return list.size();
        }

        public int getColumnCount() {
            return 2;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            SearchResult result = list.get(rowIndex);
            switch(columnIndex) {
                case 0:
                    return new GBKString(result.getArtist());
                case 1:
                    return new GBKString(result.getTitle());
                default:
                    return new GBKString("");
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return GBKString.class;
        }
    }

    private class GBKString implements Comparable<GBKString> {

        private final String content;

        public GBKString(String s) {
            if (s == null) {
                s = "";
            }
            this.content = s;
        }

        public int compareTo(WebSearchDialog.GBKString o) {
            return Collator.getInstance(Locale.CHINESE).compare(content, o.content);
        }

        public String toString() {
            return content;
        }
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        artistJT = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        titleJT = new javax.swing.JTextField();
        search = new javax.swing.JButton();
        info = new javax.swing.JLabel();
        jsp = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        save = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        fileName = new javax.swing.JTextField();
        connect = new javax.swing.JCheckBox();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("在线搜索歌词");
        setResizable(false);
        jLabel1.setText(Config.getResource("WebSearchDialog.artist"));
        artistJT.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                artistJTActionPerformed(evt);
            }
        });
        jLabel2.setText(Config.getResource("WebSearchDialog.title"));
        titleJT.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                titleJTActionPerformed(evt);
            }
        });
        search.setText(Config.getResource("WebSearchDialog.search"));
        search.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchActionPerformed(evt);
            }
        });
        info.setText(Config.getResource("WebSearchDialog.selectLyricToDownload"));
        table.getTableHeader().setReorderingAllowed(false);
        table.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tableMouseReleased(evt);
            }
        });
        jsp.setViewportView(table);
        save.setText(Config.getResource("WebSearchDialog.downLoad"));
        save.setEnabled(false);
        save.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });
        jButton3.setText(Config.getResource("WebSearchDialog.close"));
        jButton3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jLabel4.setText(Config.getResource("WebSearchDialog.saveAs"));
        connect.setSelected(true);
        connect.setText(Config.getResource("WebSearchDialog.relativeWithFile"));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jsp, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(layout.createSequentialGroup().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(artistJT, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(titleJT, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(search, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(info).addGroup(layout.createSequentialGroup().addComponent(connect).addGap(87, 87, 87).addComponent(save).addGap(18, 18, 18).addComponent(jButton3)).addGroup(layout.createSequentialGroup().addComponent(jLabel4).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(fileName, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(artistJT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel2).addComponent(titleJT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(search)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(info).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jsp, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel4).addComponent(fileName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(save).addComponent(jButton3).addComponent(connect)).addContainerGap()));
        pack();
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }

    private void searchActionPerformed(java.awt.event.ActionEvent evt) {
        doSearch();
    }

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            doSave();
        }
    }

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {
        doSave();
    }

    private void tableMouseReleased(java.awt.event.MouseEvent evt) {
        int index = table.getSelectedRow();
        if (index != -1) {
            index = sorter.convertRowIndexToModel(index);
            SearchResult result = list.get(index);
            fileName.setText(result.getArtist() + " - " + result.getTitle() + ".lrc");
        }
    }

    private void titleJTActionPerformed(java.awt.event.ActionEvent evt) {
        doSearch();
    }

    private void artistJTActionPerformed(java.awt.event.ActionEvent evt) {
        doSearch();
    }

    private void doSave() {
        info.setText(Config.getResource("WebSearchDialog.downloading"));
        new Thread() {

            public void run() {
                doSave0();
            }
        }.start();
    }

    private void doSave0() {
        int index = table.getSelectedRow();
        String path = fileName.getText();
        if (path == null || path.trim().equals("")) {
            JOptionPane.showMessageDialog(this, Config.getResource("WebSearchDialog.fileNamenotEmpty"));
            return;
        }
        if (index != -1) {
            try {
                File file = new File(Config.HOME, "Lyrics/" + path);
                if (file.isFile() && file.exists() && !Config.getConfig().isAutoOverWriteExistFile()) {
                    int i = JOptionPane.showConfirmDialog(this, Config.getResource("WebSearchDialog.existingFile") + file.getPath() + "\n" + Config.getResource("WebSearchDialog.confirmOverwrite"), Config.getResource("WebSearchDialog.confirm"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (i != JOptionPane.YES_OPTION) {
                        return;
                    }
                }
                save.setEnabled(false);
                table.setEnabled(false);
                index = sorter.convertRowIndexToModel(index);
                SearchResult result = list.get(index);
                result.save(path);
                info.setText(Config.getResource("WebSearchDialog.downloadSuccess") + file.getPath());
                Lyric lyric = new Lyric(result.getContent(), item);
                File temp = item.getLyricFile();
                lp.setLyric(lyric);
                lp.getPlayer().setLyric(lyric);
                if (connect.isSelected()) {
                    item.setLyricFile(file);
                } else {
                    item.setLyricFile(temp);
                }
            } catch (IOException ex) {
                info.setText(Config.getResource("WebSearchDialog.saveLyricFailure") + path);
                save.setEnabled(true);
                table.setEnabled(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, Config.getResource("WebSearchDialog.selectLyricToDownload"));
        }
    }

    private void doSearch() {
        search.setEnabled(false);
        table.setEnabled(false);
        save.setEnabled(false);
        artistJT.setEnabled(false);
        titleJT.setEnabled(false);
        info.setText(Config.getResource("WebSearchDialog.searching"));
        new Thread() {

            public void run() {
                doSearch0();
            }
        }.start();
    }

    private void doSearch0() {
        String artist = artistJT.getText();
        String title = titleJT.getText();
        if (artist == null) {
            artist = "";
        }
        if (title == null) {
            title = "";
        }
        list.clear();
        list.addAll(Util.getSearchResults(artist, title));
        table.revalidate();
        sorter.allRowsChanged();
        if (list.size() > 0) {
            table.setEnabled(true);
            save.setEnabled(true);
        }
        search.setEnabled(true);
        artistJT.setEnabled(true);
        titleJT.setEnabled(true);
        info.setText(Config.getResource("WebSearchDialog.selectLyricToDownload"));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                WebSearchDialog dialog = new WebSearchDialog(new javax.swing.JDialog(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    private javax.swing.JTextField artistJT;

    private javax.swing.JCheckBox connect;

    private javax.swing.JTextField fileName;

    private javax.swing.JLabel info;

    private javax.swing.JButton jButton3;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JScrollPane jsp;

    private javax.swing.JButton save;

    private javax.swing.JButton search;

    private javax.swing.JTable table;

    private javax.swing.JTextField titleJT;
}
