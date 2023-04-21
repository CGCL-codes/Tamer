package end;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.StringTokenizer;
import javax.swing.tree.DefaultMutableTreeNode;

public class RSF2Tree {

    private String in_file;

    /**
	 * Construct a transfomer which transforms the
	 * source rsf file to a tree. 
	 * 
	 * @param a_file	The source rsf file name.
	 */
    public RSF2Tree(String a_file) {
        in_file = a_file;
    }

    /**
	 * Transform the source rsf file to a tree.
	 * Reads the decomposition from the source rsf file
	 * and transforms it into a tree.
	 * 
	 * @return	The root of a tree.
	 * 
	 * @throws IOException
	 */
    public DefaultMutableTreeNode rsf2tree() throws IOException {
        DefaultMutableTreeNode result = new DefaultMutableTreeNode(in_file);
        BufferedReader in = new BufferedReader(new FileReader(in_file));
        String line = in.readLine();
        while (line != null) {
            line = line.trim();
            StringTokenizer token = new StringTokenizer(line);
            @SuppressWarnings("unused") String first_token = token.nextToken();
            String second_token = token.nextToken();
            String third_token = token.nextToken();
            if (second_token.equals(third_token)) throw new IllegalArgumentException("A node can not be the child of itself!");
            DefaultMutableTreeNode root = (DefaultMutableTreeNode) result.getRoot();
            Enumeration allNodes = root.depthFirstEnumeration();
            DefaultMutableTreeNode tok2 = null;
            DefaultMutableTreeNode tok3 = null;
            while (allNodes.hasMoreElements()) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) allNodes.nextElement();
                String item = (String) node.getUserObject();
                if (item.equalsIgnoreCase(second_token)) tok2 = node;
                if (item.equalsIgnoreCase(third_token)) tok3 = node;
            }
            if (tok2 == null) {
                tok2 = new DefaultMutableTreeNode(second_token);
                root.add(tok2);
            }
            if (tok3 == null) tok3 = new DefaultMutableTreeNode(third_token);
            tok2.add(tok3);
            line = in.readLine();
        }
        in.close();
        return result;
    }
}
