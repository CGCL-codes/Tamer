package org.seaurchin.category;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoryTree {

    private CategoryNode root;

    public CategoryTree() {
        this.root = new CategoryNode();
    }

    public void buildInitialTree() {
        String fileName = "c://seaurchin//misc//selectedcat.xml";
        try {
            BufferedReader input = new BufferedReader(new FileReader(fileName));
            try {
                String line = null;
                while ((line = input.readLine()) != null) {
                    if (line.contains("Category")) {
                        int beginIndex = line.indexOf('>') + 1;
                        int endIndex = line.lastIndexOf('<');
                        String categoryPath = line.substring(beginIndex, endIndex);
                        System.out.println(categoryPath);
                        this.addCategory(categoryPath);
                    }
                }
            } finally {
                input.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void addCategory(String categoryPath) {
        String[] catTree = categoryPath.split("/");
        CategoryNode node = this.root;
        for (String cat : catTree) {
            if (!node.hasChild(cat)) {
                node.addChild(cat);
            }
            node = node.getChild(cat);
        }
    }

    public List<String> getChildrenNames(String categoryPath) {
        String[] catTree = categoryPath.split("/");
        CategoryNode node = this.root;
        for (String cat : catTree) {
            node = node.getChild(cat);
        }
        ArrayList<String> names = new ArrayList<String>();
        for (CategoryNode child : node.getChildren()) {
            names.add(child.getName());
        }
        Collections.sort(names);
        return names;
    }

    public static void main(String args[]) {
        System.out.println("create CategoryTree");
        CategoryTree catTree = new CategoryTree();
        System.out.println("CategoryTree created");
        System.out.println("build tree");
        catTree.buildInitialTree();
        System.out.println("tree built");
        List<String> a = catTree.getChildrenNames("Top/Arts/Architecture/Building_Types");
        List<String> b = catTree.getChildrenNames("Top");
        System.out.println("a");
        for (String str : a) {
            System.out.println(str);
        }
        System.out.println("b");
        for (String str : b) {
            System.out.println(str);
        }
    }
}
