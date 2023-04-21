package edu.uno.edesk.faculty;

import static edu.uno.edesk.common.ServiceFinder.findBean;
import static edu.uno.edesk.common.ServiceFinder.getContext;
import static edu.uno.edesk.common.ServiceFinder.getSession;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.richfaces.component.html.HtmlDataTable;

public class SubmitBean implements Serializable {

    private List<Submission> submissions = new ArrayList<Submission>();

    private Set<Integer> keys = new HashSet<Integer>();

    private Submission selected;

    private List<String> javaFiles = new ArrayList<String>();

    private String viewedJava;

    private String compErr;

    private String testErr;

    private String javaContent;

    private String path;

    private SubmitFormMgr mgr;

    private HtmlDataTable richTable;

    public SubmitBean() {
        mgr = (SubmitFormMgr) findBean("SubmitFormMgr");
    }

    public void updateGrade() {
        selected = (Submission) richTable.getRowData();
        mgr.updateGrade(selected);
    }

    public synchronized void readJavaFiles() {
        javaFiles.clear();
        String newPath = getPath();
        if (getSession().getAttribute("type").toString().equals("jar")) {
            newPath = this.path + getSession().getAttribute("pkg").toString().replace('.', '/') + "/";
        }
        File uploadPath = new File(newPath);
        File[] children = uploadPath.listFiles();
        if (children != null) {
            for (int i = 0; i < children.length; i++) {
                if (!children[i].isDirectory()) {
                    int lastDot = children[i].getName().lastIndexOf(".");
                    if (children[i].getName().substring(lastDot).equals(".java")) {
                        javaFiles.add(children[i].getName());
                    }
                }
            }
        }
    }

    private String getPath() {
        String course = getSession().getAttribute("course").toString();
        String assign = getSession().getAttribute("assign").toString().replace(' ', '_');
        String unoId = this.selected.getStudent().getUnoId();
        this.path = new String("C:/EclipseGany_Wksp/edesk/upload/" + unoId + "/" + course + "/" + assign + "/");
        return this.path;
    }

    public synchronized void readJava() {
        this.viewedJava = (String) getContext().getRequestParameterMap().get("clickedJava");
        readContent(this.viewedJava, "java");
    }

    private void readContent(String fileName, String toShow) {
        String newPath = getPath();
        if (toShow.equals("java") && getSession().getAttribute("type").toString().equals("jar")) {
            newPath = this.path + getSession().getAttribute("pkg").toString().replace('.', '/') + "/";
        }
        StringBuilder content = new StringBuilder();
        try {
            FileReader fstream = new FileReader(newPath + fileName);
            BufferedReader in = new BufferedReader(fstream);
            String s;
            while ((s = in.readLine()) != null) {
                content.append(s);
                content.append("\n");
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (toShow.equals("java")) {
            this.javaContent = content.toString();
        } else if (toShow.equals("compile")) {
            this.compErr = content.toString();
        } else if (toShow.equals("test")) {
            this.testErr = content.toString();
        }
    }

    public synchronized void readCompErr() {
        readContent("compile_errors.txt", "compile");
    }

    public synchronized void readTestErr() {
        readContent("test_errors.txt", "test");
    }

    public HtmlDataTable getRichTable() {
        return richTable;
    }

    public void setRichTable(HtmlDataTable richTable) {
        this.richTable = richTable;
    }

    public String getViewedJava() {
        return viewedJava;
    }

    public void setViewedJava(String viewedJava) {
        this.viewedJava = viewedJava;
    }

    public String getJavaContent() {
        return javaContent;
    }

    public void setJavaContent(String javaContent) {
        this.javaContent = javaContent;
    }

    public List<String> getJavaFiles() {
        return javaFiles;
    }

    public List<Submission> getSubmissions() {
        if (getSession().getAttribute("leftClicked") != null) {
            submissions = mgr.getSubmits();
            for (Iterator<Submission> i = submissions.iterator(); i.hasNext(); ) {
                keys.add(i.next().getId());
            }
            getSession().setAttribute("leftClicked", null);
        }
        return submissions;
    }

    public void setSubmissions(List<Submission> submissions) {
        this.submissions = submissions;
    }

    public Set<Integer> getKeys() {
        return keys;
    }

    public void setKeys(Set<Integer> keys) {
        this.keys = keys;
    }

    public Submission getSelected() {
        return selected;
    }

    public void setSelected(Submission selected) {
        this.selected = selected;
    }

    public String getCompErr() {
        return compErr;
    }

    public void setCompErr(String compErr) {
        this.compErr = compErr;
    }

    public String getTestErr() {
        return testErr;
    }

    public void setTestErr(String testErr) {
        this.testErr = testErr;
    }
}
