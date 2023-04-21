package simpledb.GUI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;
import simpledb.remote.SimpleDriver;
import sun.misc.Regexp;
import com.trolltech.qt.core.QFile;
import com.trolltech.qt.gui.*;

public class TreeWindowHeuristic extends QWidget {

    public TreeWindowHeuristic() {
        setWindowTitle("Heuristic");
        QTreeWidget treeWidget = new QTreeWidget(this);
        treeWidget.setAnimated(true);
        treeWidget.header().hide();
        List<String> data = new ArrayList<String>();
        try {
            BufferedReader input = new BufferedReader(new FileReader("tree"));
            try {
                String line = null;
                while ((line = input.readLine()) != null) {
                    data.add(line);
                }
            } finally {
                input.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String selectPred = "";
        String joinPred = "";
        try {
            BufferedReader input = new BufferedReader(new FileReader("lastQueryLog"));
            try {
                String line1 = null;
                int i = 0;
                while ((line1 = input.readLine()) != null) {
                    System.out.println("Whiling fash7' ba2a: " + i);
                    if (i == 7) {
                        System.out.println("se7venth line is: " + line1);
                        selectPred = line1.replaceAll("The SELECT predicate is: ", "").toString();
                        i++;
                        continue;
                    } else {
                        if (i == 8) {
                            System.out.println("eighth line is: " + line1);
                            joinPred = line1.replaceAll("The JOIN predicate is: ", "").toString();
                            i++;
                            continue;
                        } else {
                            i++;
                        }
                    }
                }
            } finally {
                input.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("SSSS " + selectPred);
        System.out.println("JJJJ " + joinPred);
        String projectionList = data.get(0).toString();
        String[] tableNames = data.get(1).toString().replaceAll("[\\[\\],]", "").split(" ");
        String table1String = tableNames[0];
        String table2String = tableNames[1];
        QTreeWidgetItem projection = new QTreeWidgetItem();
        QIcon pi = new QIcon("/Something/pi.png");
        projection.setIcon(0, pi);
        projection.setText(0, projectionList);
        QTreeWidgetItem join = new QTreeWidgetItem();
        QIcon joinIcon = new QIcon("/Something/join.png");
        join.setIcon(0, joinIcon);
        join.setText(0, joinPred);
        QTreeWidgetItem table1 = new QTreeWidgetItem();
        table1.setText(0, table2String);
        QTreeWidgetItem selection = new QTreeWidgetItem();
        QIcon sigma = new QIcon("/Something/Sigma.png");
        selection.setIcon(0, sigma);
        selection.setText(0, selectPred);
        QTreeWidgetItem table2 = new QTreeWidgetItem();
        table2.setText(0, table1String);
        treeWidget.addTopLevelItem(projection);
        projection.addChild(join);
        join.addChild(table1);
        join.addChild(selection);
        selection.addChild(table2);
        treeWidget.expandAll();
        QVBoxLayout layout = new QVBoxLayout();
        layout.addWidget(treeWidget);
        setLayout(layout);
    }
}
