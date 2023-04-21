package ak.salmon;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ak.salmon.layout.topdown.TopDownFlatLayoutEngine;
import ak.salmon.layout.topdown.TopDownLayoutEngine;
import ak.salmon.layout.topdown.TopDownLayoutWithAssistantEngine;
import ak.salmon.model.DefaultNodeContent;
import ak.salmon.model.NodeContent;

public class RenderTest {

    List<NodeContent> tree1;

    List<NodeContent> tree2;

    List<NodeContent> tree3;

    List<NodeContent> tree4;

    List<NodeContent> tree5;

    List<NodeContent> tree6;

    List<NodeContent> tree7;

    List<NodeContent> tree8;

    List<NodeContent> tree9;

    List<NodeContent> tree10;

    List<NodeContent> tree11;

    List<NodeContent> tree12;

    private SalmonChartBuilder builder;

    @BeforeClass
    public void setupServices() {
        Map<String, Class<? extends SalmonLayoutEngine>> map = new HashMap<String, Class<? extends SalmonLayoutEngine>>();
        map.put("top-down", TopDownLayoutEngine.class);
        map.put("top-down-flat", TopDownFlatLayoutEngine.class);
        map.put("top-down-asistant", TopDownLayoutWithAssistantEngine.class);
        SalmonLayoutEngineFactory lef = new SalmonLayoutEngineFactory(map);
        builder = new SalmonChartBuilder(lef);
    }

    @BeforeClass
    public void setupTree1() {
        List<NodeContent> tree = new ArrayList<NodeContent>();
        tree.add(createNode(1, 0, false, "Mary", "Project Manager"));
        tree.add(createNode(2, 1, false, "John", "Team A Leader"));
        tree.add(createNode(3, 2, false, "Jacky", "Programmer"));
        tree.add(createNode(4, 2, false, "Howard", "Programmer"));
        tree.add(createNode(5, 1, false, "Patrick", "Team B Leader"));
        tree.add(createNode(6, 5, false, "Cindy", "Programmer"));
        tree.add(createNode(7, 5, false, "Gary", "Programmer"));
        tree.add(createNode(8, 1, false, "Wendy", "Requirement Analyst"));
        tree1 = tree;
    }

    @BeforeClass
    public void setupTree2() {
        List<NodeContent> tree = new ArrayList<NodeContent>();
        tree.add(createNode(1, 0, false, "Mary", "Project Manager"));
        tree.add(createNode(2, 1, false, "John", "Team A Leader"));
        tree.add(createNode(3, 2, false, "Jacky", "Programmer"));
        tree.add(createNode(4, 2, false, "Howard", "Programmer"));
        tree.add(createNode(9, 2, false, "Alex", "Programmer"));
        tree.add(createNode(10, 2, false, "Sandy", "Programmer"));
        tree.add(createNode(11, 2, false, "Teddy", "Programmer"));
        tree.add(createNode(12, 2, false, "Eric", "Programmer"));
        tree.add(createNode(5, 1, false, "Patrick", "Team B Leader"));
        tree.add(createNode(6, 5, false, "Cindy", "Programmer"));
        tree.add(createNode(7, 5, false, "Gary", "Programmer"));
        tree.add(createNode(13, 7, false, "Harry", "Programmer"));
        tree.add(createNode(14, 7, false, "Louis", "Programmer"));
        tree.add(createNode(8, 1, false, "Wendy", "Requirement Analyst"));
        tree2 = tree;
    }

    @BeforeClass
    public void setupTree3() {
        List<NodeContent> tree = new ArrayList<NodeContent>();
        tree.add(createNode(1, 0, false, "Mary", "Project Manager"));
        tree.add(createNode(2, 1, false, "John", "Team A Leader"));
        tree.add(createNode(3, 2, false, "Jacky", "Programmer"));
        tree.add(createNode(4, 2, false, "Howard", "Programmer"));
        tree.add(createNode(9, 2, false, "Alex", "Programmer"));
        tree.add(createNode(10, 2, false, "Sandy", "Programmer"));
        tree.add(createNode(11, 2, false, "Teddy", "Programmer"));
        tree.add(createNode(12, 2, false, "Eric", "Programmer"));
        tree.add(createNode(5, 1, false, "Patrick", "Team B Leader"));
        tree.add(createNode(6, 5, false, "Cindy", "Programmer"));
        tree.add(createNode(7, 5, false, "Gary", "Programmer"));
        tree.add(createNode(13, 7, false, "Harry", "Programmer"));
        tree.add(createNode(14, 7, false, "Louis", "Programmer"));
        tree.add(createNode(8, 1, false, "Wendy", "Requirement Analyst"));
        tree.add(createNode(15, 1, false, "Winnie", "Requirement Analyst"));
        tree.add(createNode(16, 1, false, "Wilson", "Requirement Analyst"));
        tree3 = tree;
    }

    @BeforeClass
    public void setupTree4() {
        List<NodeContent> tree = new ArrayList<NodeContent>();
        tree.add(createNode(1, 0, false, "Mary", "Project Manager"));
        DefaultNodeContent teamA = new DefaultNodeContent(Long.valueOf(20), Long.valueOf(1));
        teamA.addLine("Team A", "font-weight:bold");
        tree.add(teamA);
        tree.add(createNode(2, 20, false, "John", "Team A Leader"));
        tree.add(createNode(3, 2, false, "Jacky", "Programmer"));
        tree.add(createNode(4, 2, false, "Howard", "Programmer"));
        tree.add(createNode(9, 2, false, "Alex", "Programmer"));
        tree.add(createNode(10, 2, false, "Sandy", "Programmer"));
        tree.add(createNode(11, 2, false, "Teddy", "Programmer"));
        tree.add(createNode(12, 2, false, "Eric", "Programmer"));
        DefaultNodeContent teamB = new DefaultNodeContent(Long.valueOf(21), Long.valueOf(1));
        teamB.addLine("Team B", "font-weight:bold");
        tree.add(teamB);
        tree.add(createNode(5, 21, false, "Patrick", "Team B Leader"));
        tree.add(createNode(6, 5, false, "Cindy", "Programmer"));
        tree.add(createNode(7, 5, false, "Gary", "Programmer"));
        tree.add(createNode(13, 7, false, "Harry", "Programmer"));
        tree.add(createNode(14, 7, false, "Louis", "Programmer"));
        DefaultNodeContent teamC = new DefaultNodeContent(Long.valueOf(22), Long.valueOf(1));
        teamC.addLine("RA Team", "font-weight:bold");
        tree.add(teamC);
        tree.add(createNode(8, 22, false, "Wendy", "Requirement Analyst"));
        tree.add(createNode(15, 22, false, "Winnie", "Requirement Analyst"));
        tree.add(createNode(16, 22, false, "Wilson", "Requirement Analyst"));
        tree4 = tree;
    }

    @BeforeClass
    public void setupTree5() {
        List<NodeContent> tree = new ArrayList<NodeContent>();
        tree.add(createNode(1, 0, false, "Mary", "Project Manager"));
        tree.add(createNode(2, 1, true, "John", "Team A Leader"));
        tree5 = tree;
    }

    @BeforeClass
    public void setupTree6() {
        List<NodeContent> tree = new ArrayList<NodeContent>();
        tree.add(createNode(1, 0, false, "Mary", "Project Manager"));
        tree.add(createNode(2, 1, false, "Gary", "Assistant Project Manager"));
        tree.add(createNode(3, 2, false, "John", "Team A Leader"));
        tree.add(createNode(4, 3, false, "Jacky", "Programmer"));
        tree.add(createNode(5, 3, false, "Howard", "Programmer"));
        tree.add(createNode(10, 3, false, "Alex", "Programmer"));
        tree.add(createNode(11, 3, false, "Sandy", "Programmer"));
        tree.add(createNode(12, 3, false, "Teddy", "Programmer"));
        tree.add(createNode(13, 3, false, "Eric", "Programmer"));
        tree.add(createNode(6, 2, false, "Patrick", "Team B Leader"));
        tree.add(createNode(7, 6, false, "Cindy", "Programmer"));
        tree.add(createNode(8, 6, false, "Gary", "Programmer"));
        tree.add(createNode(14, 8, false, "Harry", "Programmer"));
        tree.add(createNode(15, 8, false, "Louis", "Programmer"));
        tree.add(createNode(9, 2, false, "Wendy", "Requirement Analyst"));
        tree.add(createNode(16, 2, false, "Doris", "Team C Leader"));
        tree.add(createNode(17, 16, true, "Kelly", "Executive Assistant"));
        tree.add(createNode(18, 16, false, "Tim", "C2 Team Leader"));
        tree.add(createNode(19, 16, false, "Jack", "C3 Team Leader"));
        tree.add(createNode(20, 19, false, "Gary", "Programmer"));
        tree.add(createNode(21, 19, false, "Jeff", "Programmer"));
        tree.add(createNode(22, 19, false, "Julie", "Programmer"));
        tree.add(createNode(23, 19, false, "Angel", "Programmer"));
        tree6 = tree;
    }

    @BeforeClass
    public void setupTree7() {
        List<NodeContent> tree = new ArrayList<NodeContent>();
        tree.add(createNode(1, 0, false, "Mary", "Project Manager"));
        tree7 = tree;
    }

    @BeforeClass
    public void setupTree8() {
        List<NodeContent> tree = new ArrayList<NodeContent>();
        tree.add(createNode(1, 0, false, "Mary", "Project Manager"));
        tree.add(createNode(17, 1, true, "May", "Senior Executive Assistant"));
        tree.add(createNode(18, 1, true, "Selina", "Senior Executive Assistant"));
        tree.add(createNode(19, 18, true, "Maria", "Executive Assistant"));
        tree.add(createNode(23, 17, true, "Carol", "Executive Assistant"));
        DefaultNodeContent teamA = new DefaultNodeContent(Long.valueOf(20), Long.valueOf(1));
        teamA.addLine("Team A", "font-weight:bold");
        tree.add(teamA);
        tree.add(createNode(2, 20, false, "John", "Team A Leader"));
        tree.add(createNode(24, 2, true, "Pansy", "Team A Executive Assistant"));
        tree.add(createNode(3, 2, false, "Jacky", "Programmer"));
        tree.add(createNode(4, 2, false, "Howard", "Programmer"));
        tree.add(createNode(9, 2, false, "Alex", "Programmer"));
        tree.add(createNode(10, 2, false, "Sandy", "Programmer"));
        tree.add(createNode(11, 2, false, "Teddy", "Programmer"));
        tree.add(createNode(12, 2, false, "Eric", "Programmer"));
        DefaultNodeContent teamB = new DefaultNodeContent(Long.valueOf(21), Long.valueOf(1));
        teamB.addLine("Team B", "font-weight:bold");
        tree.add(teamB);
        tree.add(createNode(5, 21, false, "Patrick", "Team B Leader"));
        tree.add(createNode(25, 5, true, "Joe", "Team B Executive Assistant"));
        tree.add(createNode(6, 5, false, "Cindy", "Programmer"));
        tree.add(createNode(7, 5, false, "Gary", "Programmer"));
        tree.add(createNode(13, 7, false, "Harry", "Programmer"));
        tree.add(createNode(14, 7, false, "Louis", "Programmer"));
        DefaultNodeContent teamC = new DefaultNodeContent(Long.valueOf(22), Long.valueOf(1));
        teamC.addLine("RA Team", "font-weight:bold");
        tree.add(teamC);
        tree.add(createNode(8, 22, false, "Wendy", "Requirement Analyst"));
        tree.add(createNode(26, 8, true, "Cherrie", "RA Team Executive Assistant"));
        tree.add(createNode(15, 22, false, "Winnie", "Requirement Analyst"));
        tree.add(createNode(16, 22, false, "Wilson", "Requirement Analyst"));
        tree8 = tree;
    }

    @BeforeClass
    public void setupTree9() {
        List<NodeContent> tree = new ArrayList<NodeContent>();
        tree.add(createNode(1, 0, false, "Mary", "Project Manager"));
        tree.add(createNode(9, 1, true, "Carol", "Executive Assistant"));
        tree.add(createNode(2, 1, false, "John", "Team A Leader"));
        tree.add(createNode(10, 2, true, "Pansy", "Executive Assistant"));
        tree.add(createNode(3, 2, false, "Jacky", "Programmer"));
        tree.add(createNode(4, 2, false, "Howard", "Programmer"));
        tree.add(createNode(5, 1, false, "Patrick", "Team B Leader"));
        tree.add(createNode(11, 5, true, "Joe", "Executive Assistant"));
        tree.add(createNode(6, 5, false, "Cindy", "Programmer"));
        tree.add(createNode(7, 5, false, "Gary", "Programmer"));
        tree.add(createNode(8, 1, false, "Wendy", "Requirement Analyst"));
        tree9 = tree;
    }

    @BeforeClass
    public void setupTree10() {
        List<NodeContent> tree = new ArrayList<NodeContent>();
        tree.add(createNode(1, 0, false, "Mary", "Project Manager"));
        tree.add(createNode(2, 1, false, "John", "Team A Leader"));
        tree.add(createNode(15, 2, true, "Pansy", "Executive Assistant"));
        tree.add(createNode(3, 2, false, "Jacky", "Programmer"));
        tree.add(createNode(4, 2, false, "Howard", "Programmer"));
        tree.add(createNode(9, 2, false, "Alex", "Programmer"));
        tree.add(createNode(10, 2, false, "Sandy", "Programmer"));
        tree.add(createNode(11, 2, false, "Teddy", "Programmer"));
        tree.add(createNode(12, 2, false, "Eric", "Programmer"));
        tree.add(createNode(5, 1, false, "Patrick", "Team B Leader"));
        tree.add(createNode(6, 5, false, "Cindy", "Programmer"));
        tree.add(createNode(7, 5, false, "Gary", "Programmer"));
        tree.add(createNode(13, 7, false, "Harry", "Programmer"));
        tree.add(createNode(14, 7, false, "Louis", "Programmer"));
        tree.add(createNode(8, 1, false, "Wendy", "Requirement Analyst"));
        tree10 = tree;
    }

    @BeforeClass
    public void setupTree11() {
        List<NodeContent> tree = new ArrayList<NodeContent>();
        tree.add(createNode(1, 0, false, "Mary", "Project Manager"));
        tree.add(createNode(2, 1, false, "John", "Team A Leader"));
        tree.add(createNode(3, 2, false, "Jacky", "Programmer"));
        tree.add(createNode(4, 2, false, "Howard", "Programmer"));
        tree.add(createNode(9, 2, false, "Alex", "Programmer"));
        tree.add(createNode(10, 2, false, "Sandy", "Programmer"));
        tree.add(createNode(11, 2, false, "Teddy", "Programmer"));
        tree.add(createNode(12, 2, false, "Eric", "Programmer"));
        tree.add(createNode(5, 1, false, "Patrick", "Team B Leader"));
        tree.add(createNode(18, 5, true, "Joe", "Executive Assistant"));
        tree.add(createNode(6, 5, false, "Cindy", "Programmer"));
        tree.add(createNode(7, 5, false, "Gary", "Programmer"));
        tree.add(createNode(13, 7, false, "Harry", "Programmer"));
        tree.add(createNode(14, 7, false, "Louis", "Programmer"));
        tree.add(createNode(8, 1, false, "Wendy", "Requirement Analyst"));
        tree.add(createNode(17, 8, true, "Cherrie", "Executive Assistant"));
        tree.add(createNode(15, 1, false, "Winnie", "Requirement Analyst"));
        tree.add(createNode(16, 1, false, "Wilson", "Requirement Analyst"));
        tree11 = tree;
    }

    @BeforeClass
    public void setupTree12() {
        List<NodeContent> tree = new ArrayList<NodeContent>();
        tree.add(createNode(1, 0, false, "Mary", "Project Manager"));
        tree.add(createNode(17, 1, true, "May", "Senior Executive Assistant"));
        tree.add(createNode(18, 1, true, "Selina", "Senior Executive Assistant"));
        tree.add(createNode(19, 18, true, "Maria", "Executive Assistant"));
        tree.add(createNode(20, 17, true, "Carol", "Executive Assistant"));
        tree.add(createNode(2, 1, false, "John", "Team A Leader"));
        tree.add(createNode(3, 2, false, "Jacky", "Programmer"));
        tree.add(createNode(4, 2, false, "Howard", "Programmer"));
        tree.add(createNode(9, 2, false, "Alex", "Programmer"));
        tree.add(createNode(10, 2, false, "Sandy", "Programmer"));
        tree.add(createNode(11, 2, false, "Teddy", "Programmer"));
        tree.add(createNode(12, 2, false, "Eric", "Programmer"));
        tree.add(createNode(5, 1, false, "Patrick", "Team B Leader"));
        tree.add(createNode(6, 5, false, "Cindy", "Programmer"));
        tree.add(createNode(7, 5, false, "Gary", "Programmer"));
        tree.add(createNode(13, 7, false, "Harry", "Programmer"));
        tree.add(createNode(14, 7, false, "Louis", "Programmer"));
        tree.add(createNode(8, 1, false, "Wendy", "Requirement Analyst"));
        tree.add(createNode(15, 1, false, "Winnie", "Requirement Analyst"));
        tree.add(createNode(16, 1, false, "Wilson", "Requirement Analyst"));
        tree12 = tree;
    }

    private NodeContent createNode(int id, int parentId, boolean assistantStatus, String... lines) {
        Long sParentId = Long.valueOf(parentId);
        if (parentId == 0) sParentId = null;
        DefaultNodeContent dnc = new DefaultNodeContent(Long.valueOf(id), sParentId);
        dnc.setAssistantStatus(assistantStatus);
        for (int i = 0; i < lines.length; i++) {
            dnc.addLine(lines[i]);
        }
        return dnc;
    }

    @Test
    public void checkDataInitialized() {
        assert tree1 != null : "Data tree1 not initialized.";
        assert tree2 != null : "Data tree2 not initialized.";
        assert tree3 != null : "Data tree3 not initialized.";
        assert tree4 != null : "Data tree4 not initialized.";
    }

    @Test
    public void drawTree1() throws IOException {
        String fileName = "examples/tree1.svg";
        FileWriter f = new FileWriter(fileName);
        f.write(builder.render(tree1, "top-down"));
        f.close();
        System.out.println("File " + fileName + " written.  Please check it manually.");
    }

    @Test
    public void drawTree2() throws IOException {
        String fileName = "examples/tree2.svg";
        FileWriter f = new FileWriter(fileName);
        f.write(builder.render(tree2, "top-down"));
        f.close();
        System.out.println("File " + fileName + " written.  Please check it manually.");
    }

    @Test
    public void drawTree3() throws IOException {
        String fileName = "examples/tree3.svg";
        FileWriter f = new FileWriter(fileName);
        f.write(builder.render(tree3, "top-down"));
        f.close();
        System.out.println("File " + fileName + " written.  Please check it manually.");
    }

    @Test
    public void drawTree4() throws IOException {
        String fileName = "examples/tree4.svg";
        FileWriter f = new FileWriter(fileName);
        f.write(builder.render(tree4, "top-down"));
        f.close();
        System.out.println("File " + fileName + " written.  Please check it manually.");
    }

    @Test
    public void drawTree5() throws IOException {
        String fileName = "examples/tree5.svg";
        FileWriter f = new FileWriter(fileName);
        f.write(builder.render(tree1, "top-down-asistant"));
        f.close();
        System.out.println("File " + fileName + " written.  Please check it manually.");
    }

    @Test
    public void drawTree6() throws IOException {
        String fileName = "examples/tree6.svg";
        FileWriter f = new FileWriter(fileName);
        f.write(builder.render(tree2, "top-down-asistant"));
        f.close();
        System.out.println("File " + fileName + " written.  Please check it manually.");
    }

    @Test
    public void drawTree7() throws IOException {
        String fileName = "examples/tree7.svg";
        FileWriter f = new FileWriter(fileName);
        f.write(builder.render(tree3, "top-down-asistant"));
        f.close();
        System.out.println("File " + fileName + " written.  Please check it manually.");
    }

    @Test
    public void drawTree8() throws IOException {
        String fileName = "examples/tree8.svg";
        FileWriter f = new FileWriter(fileName);
        f.write(builder.render(tree4, "top-down-asistant"));
        f.close();
        System.out.println("File " + fileName + " written.  Please check it manually.");
    }

    @Test
    public void drawTree9() throws IOException {
        String fileName = "examples/tree9.svg";
        FileWriter f = new FileWriter(fileName);
        f.write(builder.render(tree5, "top-down-asistant"));
        f.close();
        System.out.println("File " + fileName + " written.  Please check it manually.");
    }

    @Test
    public void drawTree10() throws IOException {
        String fileName = "examples/tree10.svg";
        FileWriter f = new FileWriter(fileName);
        f.write(builder.render(tree6, "top-down-asistant"));
        f.close();
        System.out.println("File " + fileName + " written.  Please check it manually.");
    }

    @Test
    public void drawTree11() throws IOException {
        String fileName = "examples/tree11.svg";
        FileWriter f = new FileWriter(fileName);
        f.write(builder.render(tree7, "top-down-asistant"));
        f.close();
        System.out.println("File " + fileName + " written.  Please check it manually.");
    }

    @Test
    public void drawTree12() throws IOException {
        String fileName = "examples/tree12.svg";
        FileWriter f = new FileWriter(fileName);
        f.write(builder.render(tree8, "top-down-asistant"));
        f.close();
        System.out.println("File " + fileName + " written.  Please check it manually.");
    }

    @Test
    public void drawTree13() throws IOException {
        String fileName = "examples/tree13.svg";
        FileWriter f = new FileWriter(fileName);
        f.write(builder.render(tree9, "top-down-asistant"));
        f.close();
        System.out.println("File " + fileName + " written.  Please check it manually.");
    }

    @Test
    public void drawTree14() throws IOException {
        String fileName = "examples/tree14.svg";
        FileWriter f = new FileWriter(fileName);
        f.write(builder.render(tree10, "top-down-asistant"));
        f.close();
        System.out.println("File " + fileName + " written.  Please check it manually.");
    }

    @Test
    public void drawTree15() throws IOException {
        String fileName = "examples/tree15.svg";
        FileWriter f = new FileWriter(fileName);
        f.write(builder.render(tree11, "top-down-asistant"));
        f.close();
        System.out.println("File " + fileName + " written.  Please check it manually.");
    }

    @Test
    public void drawTree16() throws IOException {
        String fileName = "examples/tree16.svg";
        FileWriter f = new FileWriter(fileName);
        f.write(builder.render(tree12, "top-down-asistant"));
        f.close();
        System.out.println("File " + fileName + " written.  Please check it manually.");
    }

    @Test
    public void drawTree4Flat() throws IOException {
        String fileName = "examples/tree4flat.svg";
        FileWriter f = new FileWriter(fileName);
        f.write(builder.render(tree4, "top-down-flat"));
        f.close();
        System.out.println("File " + fileName + " written.  Please check it manually.");
    }
}
