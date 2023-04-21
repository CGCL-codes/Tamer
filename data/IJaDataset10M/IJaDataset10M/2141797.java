package tools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import main.Main;
import main.mapobjects.Tile;

@SuppressWarnings("serial")
public class TileSetEditor extends JPanel {

    DefaultTableModel data;

    JTable table;

    JScrollPane scrollpane;

    ImageDisp disp;

    final int IMGFAC = 3;

    final int IMGSIZE = IMGFAC * Main.TILESIZE;

    boolean haschanged = false;

    boolean isvalid = true;

    String columnNames[] = { "ID", "Group Name", "Sub Name", "Pass Value", "File Name", "Image Found?" };

    Object[][] entries;

    int entrynum;

    ArrayList<Image> images = new ArrayList<Image>();

    ArrayList<Object[]> imagestoadd = new ArrayList<Object[]>();

    private int type;

    /**
	 * Creates a new TileSetEditor object to be used to edit the tile set files.
	 * Whether it edits the background tile set, the foreground tile set, or the
	 * object tile set is determined by {@line type}.
	 * @param type Indicates whether this object edits the background tile set,
	 * foreground tile set, or the obect set. Values from Tile.BTILE,FTILE,OTILE
	 */
    public TileSetEditor(int type) {
        this.type = type;
        loadFile();
        setUpGui();
        setUpLayout();
        new TileSetEditorFrame().setVisible(true);
    }

    private void setUpLayout() {
        this.setLayout(null);
        scrollpane.setBounds(0, 0, 500, 500);
        disp.setBounds(500, 0, 200, 500);
        this.add(scrollpane);
        this.add(disp);
        this.setSize(700, 600);
    }

    private void setUpGui() {
        scrollpane = new JScrollPane();
        table = new JTable();
        data = new DefaultTableModel(entries, columnNames) {

            boolean[] canEdit = new boolean[] { false, true, true, true, false, false };

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
        table.setModel(data);
        table.setRowSelectionAllowed(true);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);
        table.setGridColor(Color.BLACK);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.doLayout();
        disp = new ImageDisp();
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                disp.shownew(table.getSelectedRow());
            }
        });
        scrollpane.setViewportView(table);
    }

    /**
	 * Function loads the tile set file for whichever MapElement type was set through {@link type}
	 * in the class constructor.
	 * <p>
	 * If the file is missing it returns immediately. 
	 * If the number of entries listed at the top of the file doesn't match the actual number of
	 * entries, it prints out an error message but still functions.
	 * If a single entry is corrupted or incorrectly formatted it just skips it and prints out a
	 * warning.
	 */
    private void loadFile() {
        String file = "";
        if (type == Tile.BTILE) file = Main.BTILE_LIST_NAME; else if (type == Tile.FTILE) file = Main.FTILE_LIST_NAME; else if (type == Tile.OTILE) file = Main.OTILE_LIST_NAME; else return;
        Scanner scan;
        try {
            scan = new Scanner(new File(file));
        } catch (FileNotFoundException e1) {
            return;
        }
        entrynum = Integer.parseInt(scan.nextLine());
        entries = new Object[entrynum][];
        int count = 0;
        String entryline[];
        String fname, groupname, subname;
        boolean endoffile = false;
        while (!endoffile) {
            try {
                entryline = scan.nextLine().split(",");
                int id = Integer.parseInt(entryline[0]);
                fname = entryline[1];
                groupname = fname.substring(0, fname.indexOf('_'));
                subname = fname.substring(fname.indexOf('_') + 1);
                int pass = Integer.parseInt(entryline[2]);
                if (id != (count)) {
                    System.out.println("Tile ID's are out of order!");
                    isvalid = false;
                }
                try {
                    BufferedImage img = ImageIO.read(new File(Main.getImageFile(id, fname, this.type)));
                    if (img != null) {
                        images.add((count), img.getScaledInstance(IMGSIZE, IMGSIZE, Image.SCALE_DEFAULT));
                        entries[count] = new Object[] { id, groupname, subname, pass, new FileName(count), true };
                    } else throw new IOException();
                } catch (IOException e) {
                    System.out.println("Couldn't load image file for " + fname);
                    images.add((count), null);
                    entries[count] = new Object[] { id, groupname, subname, pass, new FileName(count), false };
                    isvalid = false;
                }
                count++;
            } catch (NumberFormatException e) {
                System.out.println("Error: tile file was corrupted. Could not read!");
                isvalid = false;
            } catch (NoSuchElementException e) {
                if (count != entrynum) System.out.println("Error: In loading tile file: Number of item entries did not match listed number of item");
                isvalid = false;
                endoffile = true;
            }
        }
    }

    private boolean checkvalid() {
        boolean check = true;
        if (images.size() != data.getRowCount()) throw new ProgrammingError("images ArrayList has different size (" + images.size() + ") than num of rows (" + data.getRowCount() + ")");
        for (int i = 0; i < data.getRowCount(); i++) {
            if (images.get(i) == null) {
                check = false;
                data.setValueAt(false, i, 5);
            }
        }
        for (int i = 0; i < imagestoadd.size(); i++) {
            try {
                ImageIO.read(new File((String) imagestoadd.get(i)[1]));
            } catch (IOException e) {
                data.setValueAt(false, i, 5);
                check = false;
            }
        }
        return check;
    }

    private static File lastfile;

    private void chooseImage() {
        if (table.getSelectedRow() == -1) return;
        JFileChooser jfc = new JFileChooser("Choose Image File");
        if (lastfile != null) jfc.setSelectedFile(lastfile); else jfc.setSelectedFile(new File(Main.FILEDIR));
        int val = jfc.showOpenDialog(this);
        if (val == 0) {
            lastfile = jfc.getCurrentDirectory();
            int row = table.getSelectedRow();
            File imgfile = jfc.getSelectedFile();
            try {
                Image img = ImageIO.read(imgfile);
                if (img == null) throw new IOException("Image read returned null image");
                images.set(row, img.getScaledInstance(IMGSIZE, IMGSIZE, Image.SCALE_SMOOTH));
                imagestoadd.add(new Object[] { row, imgfile.getPath() });
                data.setValueAt(true, row, 5);
                disp.shownew(row);
            } catch (IOException e) {
                System.out.println("Must be a valid image files");
            }
        }
    }

    private void addEntryToEnd() {
        int rows = data.getRowCount();
        data.addRow(new Object[] { rows, null, null, 0, null, false });
        images.add(rows, null);
    }

    private void removeEntryFromEnd() {
        data.removeRow(data.getRowCount());
        images.remove(data.getRowCount());
    }

    private void saveFile() {
        System.out.println("***********Warning************");
        System.out.println("The save function is still in beta and is not guarenteed to work");
        System.out.println("In order to keep SVN compatibility, saving currently relies on calling external commands which is always risky with java");
        System.out.println("It would be prudent to run the command svn status in the files dir afterwards to ensure everything is okay");
        System.out.println("If svn status yields any files with ? marks to the left of their names, it would be wise to backup your changes and call svn revert");
        System.out.println("In the future this program will use a native java svn library to prevent this");
        System.out.println("Also the programmer is advised to commit any prior changes made before proceeding in case something goes wrong");
        System.out.println("Type yes to continue, type no to cancel the save");
        boolean stop = false;
        while (!stop) {
            System.out.print("(yes/no): ");
            String resp = main.Main.userin.nextLine();
            if (resp.compareToIgnoreCase("yes") == 0) {
                stop = true;
            } else if (resp.compareToIgnoreCase("no") == 0) return;
        }
        if (!checkvalid()) {
            JOptionPane.showMessageDialog(this, "Can't save because not all tiles were linked to valid images");
            return;
        }
        int ecount = 0;
        for (int i = 0; i < entries.length; i++) {
            if (entries[i] == null) break;
            ecount++;
            boolean changerow = false;
            for (int j = 0; j < entries[i].length; j++) {
                if (table.getValueAt(i, j).toString().compareTo(entries[i][j].toString()) != 0) {
                    changerow = true;
                }
            }
            if (changerow) {
                int id = ((Integer) entries[i][0]).intValue();
                int type = this.type;
                String ofile = (Main.getImageFile(id, entries[i][1] + "_" + entries[i][2], this.type));
                String nfile = (Main.getImageFile(id, table.getValueAt(i, 4).toString(), this.type));
                if (!(ofile.compareTo(nfile) == 0)) if (!Tools.svnmove(ofile, nfile)) System.out.println(String.format("svn move of %s to %s failed. You might want to try to enter this command manually in terminal", ofile, nfile));
            }
        }
        String fname;
        if (type == Tile.BTILE) fname = Main.BTILE_LIST_NAME; else if (type == Tile.FTILE) fname = Main.FTILE_LIST_NAME; else if (type == Tile.OTILE) fname = Main.OTILE_LIST_NAME; else throw new ProgrammingError("TileSetEditor object has illegal type value");
        try {
            System.out.println("Table Rows: " + table.getRowCount());
            System.out.println("Entry Rows: " + ecount);
            PrintStream ps = new PrintStream(new FileOutputStream(fname));
            ps.print(ecount + "\n");
            for (int i = 0; i < data.getRowCount(); i++) {
                ps.print(table.getValueAt(i, 0) + "," + table.getValueAt(i, 4).toString() + "," + table.getValueAt(i, 3) + ",\n");
            }
            ps.flush();
            ps.close();
            System.out.println("Creation of tile set file suceeded");
            for (int i = 0; i < imagestoadd.size(); i++) {
                Object temp[] = imagestoadd.get(i);
                int row = ((Integer) temp[0]).intValue();
                String imgfname = Main.getImageFile(((Integer) table.getValueAt(row, 0)).intValue(), table.getValueAt(row, 4).toString(), TileSetEditor.this.type);
                if (Tools.svncopy((String) imagestoadd.get(i)[1], imgfname)) {
                    System.out.println(String.format("svn copy of %s to %s seems like it worked", (String) imagestoadd.get(i)[1], imgfname));
                } else {
                    System.out.println("Couldn't copy image file");
                }
            }
            System.out.println("All changes must be commited manually");
            System.out.println("Once again I caution you to run svn status first to make sure everything worked");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TileSetEditor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    class FileName {

        int row;

        public FileName(int r) {
            this.row = r;
        }

        @Override
        public String toString() {
            return table.getValueAt(row, 1) + "_" + table.getValueAt(row, 2);
        }
    }

    class TileSetEditorFrame extends JFrame {

        JMenuBar menubar;

        JMenu filemenu;

        JMenu editmenu;

        JMenu addentrysubmenu;

        public TileSetEditorFrame() {
            setUpMenu();
            this.add(TileSetEditor.this);
            this.setSize(TileSetEditor.this.getWidth(), TileSetEditor.this.getHeight() + menubar.getHeight());
            Tools.centerJFrame(this);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        private void setUpMenu() {
            menubar = new JMenuBar();
            filemenu = new JMenu("File");
            filemenu.setMnemonic(KeyEvent.VK_F);
            JMenuItem saveitem = new JMenuItem("Save Tile Set");
            saveitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
            saveitem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    TileSetEditor.this.saveFile();
                }
            });
            filemenu.add(saveitem);
            JMenuItem closeitem = new JMenuItem("Close Program");
            closeitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
            filemenu.add(closeitem);
            editmenu = new JMenu("Edit");
            editmenu.setMnemonic(KeyEvent.VK_E);
            JMenuItem pickimage = new JMenuItem("Choose Image for Entry");
            editmenu.add(pickimage);
            pickimage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK));
            pickimage.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    TileSetEditor.this.chooseImage();
                }
            });
            JMenuItem addtoend = new JMenuItem("Add to End", KeyEvent.VK_A);
            addtoend.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
            addtoend.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    TileSetEditor.this.addEntryToEnd();
                }
            });
            editmenu.add(addtoend);
            JMenuItem removeend = new JMenuItem("Remove Last Entry", KeyEvent.VK_R);
            removeend.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
            removeend.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    TileSetEditor.this.removeEntryFromEnd();
                }
            });
            menubar.add(filemenu);
            menubar.add(editmenu);
            this.setJMenuBar(menubar);
        }
    }

    class ImageDisp extends JPanel {

        JPanel disppanel;

        JButton choose;

        final int edge = (200 - IMGSIZE) / 2;

        int crow;

        public ImageDisp() {
            crow = -1;
            this.setLayout(null);
            disppanel = new JPanel() {

                @Override
                public void paintComponent(Graphics g) {
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(0, 0, IMGSIZE, IMGSIZE);
                    if (crow > -1 && crow < images.size() && images.get(crow) != null) {
                        g.drawImage(images.get(crow), 0, 0, null);
                    }
                }
            };
            disppanel.setBounds(edge, edge, IMGSIZE, IMGSIZE);
            choose = new JButton("Choose Image");
            choose.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    TileSetEditor.this.chooseImage();
                }
            });
            choose.setBounds((200 - choose.getPreferredSize().width) / 2, edge + IMGSIZE, choose.getPreferredSize().width, choose.getPreferredSize().height);
            this.add(disppanel);
            this.add(choose);
        }

        public void shownew(int row) {
            this.crow = row;
            disppanel.repaint();
        }
    }

    public static void start() {
        JFrame frame = new JFrame("Tile Set Editor");
        JLabel label = new JLabel("Which tile set?");
        JButton back = new JButton("Background");
        back.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new TileSetEditor(Tile.BTILE);
            }
        });
        JButton front = new JButton("Foreground");
        front.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new TileSetEditor(Tile.FTILE);
            }
        });
        JButton item = new JButton("Item");
        item.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new TileSetEditor(Tile.OTILE);
            }
        });
        int max = 0;
        if (back.getPreferredSize().width > max) max = back.getPreferredSize().width;
        if (front.getPreferredSize().width > max) max = front.getPreferredSize().width;
        if (item.getPreferredSize().width > max) max = item.getPreferredSize().width;
        frame.getContentPane().setLayout(null);
        int w = max * 3;
        int bh = item.getPreferredSize().height;
        int lh = label.getPreferredSize().height;
        int h = bh + lh;
        label.setLocation((w - label.getPreferredSize().width) / 2, 0);
        label.setSize(label.getPreferredSize());
        frame.add(label);
        back.setLocation(0, lh);
        back.setSize(max, bh);
        front.setLocation(max, lh);
        front.setSize(max, bh);
        item.setLocation(2 * max, lh);
        item.setSize(max, bh);
        frame.add(label);
        frame.add(back);
        frame.add(front);
        frame.add(item);
        frame.setSize(max * 3, h + 20);
        Tools.centerJFrame(frame);
        frame.setVisible(true);
    }
}
