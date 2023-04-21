package rezine;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class RezineFrame extends JFrame implements ActionListener {

    private static final long serialVersionUID = Rezine.VERSION.hashCode();

    private static final String DEPENDENCIES = "dependencies";

    private static final String MAVENIZE = "mavenize";

    private static final String CREATE = "create";

    private static final String SUMMARIZE = "summarize";

    private JTextField tgroup;

    private JTextField tlibpath;

    private JTextField trepo;

    private JList current_libs;

    private JList found_libs;

    private JLabel status = new JLabel("Status");

    private JButton dep;

    private Rezine mvnizer;

    public RezineFrame(Rezine mvnizer) {
        super(Rezine.NAME);
        this.mvnizer = mvnizer;
        tgroup = new JTextField(mvnizer.getGroupid(), 20);
        tlibpath = new JTextField(mvnizer.getLibpath(), 20);
        trepo = new JTextField(mvnizer.getRepository(), 20);
        JButton launch = new JButton("Mavenize");
        launch.setName(MAVENIZE);
        launch.addActionListener(this);
        JButton create = new JButton("Create Missing Libs");
        create.setName(CREATE);
        create.addActionListener(this);
        JButton sum = new JButton("Summary");
        sum.setName(SUMMARIZE);
        sum.addActionListener(this);
        dep = new JButton("Dependencies");
        dep.setName(DEPENDENCIES);
        dep.setEnabled(false);
        dep.addActionListener(this);
        JButton chlp = new JButton("...");
        chlp.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                updateLibDirectory(tlibpath.getText());
            }
        });
        JButton chrp = new JButton("...");
        chrp.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                updateRepoDirectory(trepo.getText());
            }
        });
        JPanel options = new JPanel(new FlowLayout());
        options.setPreferredSize(new Dimension(305, 150));
        options.add(new JLabel("Group ID :"));
        options.add(tgroup);
        options.add(new JLabel("Project lib path :"));
        options.add(tlibpath);
        options.add(chlp);
        options.add(new JLabel("Repository :"));
        options.add(trepo);
        options.add(chrp);
        options.add(launch);
        options.add(dep);
        options.add(sum);
        options.add(create);
        JPanel main = new JPanel(new BorderLayout());
        JPanel top = new JPanel(new BorderLayout());
        top.add(options, BorderLayout.WEST);
        top.add(createCurrentList(), BorderLayout.EAST);
        main.add(createFoundList(), BorderLayout.CENTER);
        main.add(status, BorderLayout.SOUTH);
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, top, main);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(150);
        getContentPane().add(splitPane);
        setJMenuBar(createMenu());
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void setStatus(String text) {
        status.setText(text);
    }

    private JScrollPane createCurrentList() {
        current_libs = new JList();
        current_libs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane jsp = new JScrollPane(current_libs);
        current_libs.setCellRenderer(new ListCellRenderer() {

            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = new JLabel("");
                if (value instanceof File) {
                    label.setText(((File) value).getName());
                }
                label.setForeground(isSelected ? Color.RED : current_libs.getForeground());
                return label;
            }
        });
        current_libs.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                Object o = current_libs.getSelectedValue();
                if (o instanceof File) {
                    String check = Utils.checksum((File) o);
                    String current_cheksum = mvnizer.getCurrent_cheksum();
                    if (current_cheksum == null || !check.equals(current_cheksum)) {
                        mvnizer.setCurrent_cheksum(check);
                        updateList(found_libs, mvnizer.getPossibleMatches((File) o).toArray());
                        found_libs.setSelectedValue(mvnizer.getMatches().get((File) o), true);
                    }
                }
            }
        });
        return jsp;
    }

    private JScrollPane createFoundList() {
        found_libs = new JList();
        found_libs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        found_libs.setCellRenderer(new ListCellRenderer() {

            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JCheckBox j = new JCheckBox("" + value);
                j.setBackground(Color.WHITE);
                String current_cheksum = mvnizer.getCurrent_cheksum();
                String check = Utils.checksum((File) value);
                if (check == null || (value instanceof File && !((File) value).exists())) {
                    j.setText("<html>" + j.getText() + " <font color=\"black\">MUST BE CREATED</font>");
                    j.setBackground(Color.RED);
                } else if (current_cheksum != null && check.equals(current_cheksum)) {
                    j.setText("<html>" + j.getText() + " <font color=\"red\">HASH MATCH FOUND !!</font>");
                    j.setBackground(Color.GREEN);
                }
                j.setSelected(isSelected);
                return j;
            }
        });
        found_libs.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                Object o = found_libs.getSelectedValue();
                if (o != null && current_libs.getSelectedIndex() >= 0) {
                    mvnizer.select((File) current_libs.getSelectedValue(), (File) o);
                }
            }
        });
        return new JScrollPane(found_libs);
    }

    private JMenuBar createMenu() {
        JMenuBar jmb = new JMenuBar();
        JMenu jm = new JMenu("File");
        jmb.add(jm);
        JMenuItem jmi = new JMenuItem("Choose Project");
        jmi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                updateLibDirectory(tlibpath.getText());
            }
        });
        jm.add(jmi);
        jmi = new JMenuItem("Close");
        jm.add(jmi);
        jmi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        jm = new JMenu("Generate");
        jmb.add(jm);
        jmi = new JMenuItem("Mavenize !");
        jmi.setName(MAVENIZE);
        jmi.addActionListener(this);
        jm.add(jmi);
        jmi = new JMenuItem("Dependencies");
        jmi.setName(DEPENDENCIES);
        jm.add(jmi);
        jmi = new JMenuItem("Summarize");
        jmi.setName(SUMMARIZE);
        jm.add(jmi);
        jmi.addActionListener(this);
        jm = new JMenu("Help");
        jmi = new JMenuItem("About " + Rezine.NAME);
        jmi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(RezineFrame.this, "<html><center><h1>" + Rezine.NAME + "</h1>Author : Arnaud Saval<br/><a href=\"mailto:arnaud.saval@gmail.com\">arnaud.saval@gmail.com</a><br/>Version : " + Rezine.VERSION + "</center>", "About Mavenizer", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        jm.add(jmi);
        jmb.add(jm);
        return jmb;
    }

    private void updateList(JList list, Object[] newdata) {
        DefaultListModel dlm = new DefaultListModel();
        if (newdata == null) {
            return;
        }
        for (int i = 0; i < newdata.length; i++) {
            if (newdata[i] != null) {
                dlm.add(i, newdata[i]);
            }
        }
        list.setModel(dlm);
    }

    private File chooseDir(String current) {
        JFileChooser chooser = new JFileChooser(current);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int response = chooser.showOpenDialog(this);
        if (response == JFileChooser.APPROVE_OPTION) {
            File dir = chooser.getSelectedFile();
            if (dir.isDirectory()) {
                return dir;
            }
        }
        return null;
    }

    private void updateRepoDirectory(String repo) {
        File dir = chooseDir(repo);
        if (dir != null) {
            trepo.setText(dir.getAbsolutePath());
            mvnizer.setRepository(dir.getAbsolutePath());
            dep.setEnabled(false);
        }
    }

    private void updateLibDirectory(String lib) {
        File dir = chooseDir(lib);
        if (dir != null && dir.isDirectory()) {
            boolean pom = false;
            for (File f : dir.listFiles()) {
                if (f.getName().matches("pom.xml")) {
                    pom = true;
                    break;
                }
            }
            if (pom) {
                int result = JOptionPane.showConfirmDialog(this, "<html>This project conatins a pom file!<br>Do you want to export this project's dependencies into a new repository ?<br>If you answer yes, you will be ask to select a new directory to save your dependencies copies.<br>WARN: make sure the path to your repository is correctly set.");
                if (result == JOptionPane.YES_OPTION) {
                    makeDependenciesCopy(dir.getAbsoluteFile());
                    return;
                }
            }
            tlibpath.setText(dir.getAbsolutePath());
            mvnizer.setLibpath(dir.getAbsolutePath());
            dep.setEnabled(false);
        }
    }

    /**
	 * This function allows you to export all the dependencies of a project 
	 * with respect to the repository structure (including pom, source and doc)
	 * 
	 * @param absoluteDir a dir to a java maven project
	 */
    private void makeDependenciesCopy(File absoluteDir) {
        File destination = chooseDir(absoluteDir.getAbsolutePath());
        StringWriter sw = new StringWriter();
        try {
            File cp = new File(absoluteDir.getAbsolutePath() + "/mvndbcp.bat");
            FileWriter fw = new FileWriter(cp);
            fw.write("cd " + absoluteDir.getAbsolutePath() + "\n");
            fw.write("mvn dependency:build-classpath\n");
            fw.close();
            Process proc = Runtime.getRuntime().exec(absoluteDir.getAbsolutePath() + "/mvndbcp.bat");
            BufferedInputStream bi = new BufferedInputStream(proc.getInputStream());
            int c;
            while ((c = bi.read()) != -1) {
                sw.append((char) c);
            }
            bi.close();
            cp.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String s = sw.toString();
        int start = s.indexOf("Dependencies classpath:");
        int end = s.indexOf("[INFO] BUILD SUCCESSFUL");
        if (start == -1 || end == -1) {
            JOptionPane.showMessageDialog(this, "Dependencies retrieval failed");
            return;
        }
        s = s.substring(start, end);
        String[] p = s.split("\n");
        if (p.length != 3) {
            JOptionPane.showMessageDialog(this, "Dependencies retrieval failed");
            return;
        }
        s = p[1];
        String sep = System.getProperty("path.separator").equals(":") ? ":" : ";";
        String dsep = System.getProperty("file.separator");
        p = s.split(sep);
        String repo = trepo.getText();
        File cp = new File(absoluteDir.getAbsolutePath() + dsep + "rezine_copy.bat");
        try {
            FileWriter fw = new FileWriter(cp);
            for (String d : p) {
                File fd = new File(d);
                String base = d.substring(0, d.length() - fd.getName().length());
                if (base.endsWith(dsep)) {
                    base = base.substring(0, base.length() - 1);
                }
                String to = destination + dsep + base.substring(repo.length());
                System.out.println(fd.getAbsolutePath() + " to " + to);
                new File(to).mkdirs();
                for (String ext : new String[] { ".jar", "-javadoc.jar", "-sources.jar", ".pom" }) {
                    String name = fd.getName();
                    int z = name.lastIndexOf('.');
                    if (z != -1) {
                        name = name.substring(0, z);
                    }
                    name += ext;
                    if (new File(base + dsep + name).exists()) {
                        fw.write("copy " + base + dsep + name + " " + to + dsep + name + "\r\n");
                    } else {
                        System.out.println("[WARN] " + base + dsep + name + " does not exist.");
                    }
                }
            }
            fw.close();
            Process proc = Runtime.getRuntime().exec(absoluteDir.getAbsolutePath() + dsep + "rezine_copy.bat");
            BufferedInputStream bi = new BufferedInputStream(proc.getInputStream());
            while (bi.read() != -1) {
            }
            bi.close();
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        cp.delete();
    }

    public void actionPerformed(ActionEvent e) {
        String name = ((JComponent) e.getSource()).getName();
        if (name.equals(DEPENDENCIES)) {
            JTextArea ta = new JTextArea(mvnizer.generateDependencies());
            ta.setEditable(false);
            ta.setBackground(Color.WHITE);
            JScrollPane jsp = new JScrollPane(ta);
            jsp.setPreferredSize(new Dimension(300, 300));
            JOptionPane.showMessageDialog(RezineFrame.this, jsp);
        } else if (name.equals(CREATE)) {
            if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(this, "Do you really want to add missing libraries to your repository : " + mvnizer.getRepository())) {
                String resultats = mvnizer.generateMissingDependencies();
                if (resultats.equals("")) {
                    resultats = "There is no need to create new libraries";
                }
                resultats = "<html>" + resultats.replaceAll("\n", "<br>");
                JOptionPane.showMessageDialog(this, resultats);
                summarize();
            }
        } else if (name.equals(MAVENIZE)) {
            mavenize();
            summarize();
        } else if (name.equals(SUMMARIZE)) {
            summarize();
        }
    }

    private void mavenize() {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        mvnizer.setLibpath(tlibpath.getText());
        mvnizer.setGroupid(tgroup.getText());
        mvnizer.setRepository(trepo.getText());
        setStatus("Repository crawling");
        mvnizer.mavenize();
        setStatus("Mavenization done; Choose your libraries.");
        updateList(current_libs, mvnizer.getOrglibs());
        dep.setEnabled(true);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    private void summarize() {
        current_libs.clearSelection();
        mvnizer.setCurrent_cheksum(null);
        Map<File, File> map = mvnizer.getMatches();
        List<File> values = new LinkedList<File>(map.values());
        Collections.sort(values);
        int i = 0;
        int j = 0;
        for (File f : map.keySet()) {
            if (!map.get(f).exists()) {
                i++;
            } else if (!Utils.checksum(f).equals(Utils.checksum(map.get(f)))) {
                System.out.println(f + " " + map.get(f));
                System.out.println(Utils.checksum(f) + " " + Utils.checksum(map.get(f)));
                j++;
            }
        }
        setStatus(i == 0 ? "<html>All dependencies have been found" + (j == 0 ? "" : " but <font color=\"red\">some hashes do not match</red>") + "." : "<html><font color=\"red\">Some dependency do not exist" + (j == 0 ? "" : " and some other hashes do not match</font>") + ".");
        updateList(found_libs, values.toArray());
    }
}
