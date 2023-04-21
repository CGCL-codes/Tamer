package com.taobao.b2c.neaten;

import java.util.List;

/**
 * @author xiaoxie
 * @create time��2008-5-8 ����02:11:43
 * @description
 */
public interface Node {

    public static String FORMAT_TYPE_OPEN = "1";

    public static String FORMAT_TYPE_CLOSE = "2";

    public static String FORMAT_TYPE_CLOSE_OPEN = "4";

    public static String FORMAT_TYPE_INTACT = "3";

    public static String FORMAT_TYPE_OPEN_FIRST_LINE = "5";

    public static String CONTENT_TYPE_CONTENT = "1";

    public static String CONTENT_TYPE_CODE = "2";

    public static String CONTENT_TYPE_COMMENT = "3";

    public static String CONTENT_TYPE_TAG = "4";

    public static String FORMAT_TYPE_CONTENT = "6";

    public static String DEFUALT_TAG_NODE = "default_tag_node";

    public static String COMMENT_NODE = "comment_node";

    public static String JAVA_SCRIPT_NODE = "java_script_node";

    public static String VM_SCRIPT_NODE = "vm_script_node";

    int getLine();

    boolean isEmpty();

    void setLine(int line);

    String getStartDelimiter();

    String getEndDelimiter();

    String getName();

    void setName(String name);

    void setStartDelimiter(String endDelimiter);

    void setEndDelimiter(String endDelimiter);

    String getOriginalSource();

    public void setOriginalSource(String originalSource);

    Node getParentNode();

    void setParentNode(Node node);

    Node getNextNode();

    void setNextNode(Node node);

    Node getPreviousNode();

    void setPreviousNode(Node node);

    List<Node> getChildrenNodes();

    void setChildrenNodes(List<Node> children);

    void addChildrenNode(Node child);

    void setClosed(boolean status);

    boolean isClosed();

    String getIndend();

    void setIndend(String indend);

    String getFormatType();

    public void setFormatType(String type);

    String getContentType();

    public void setContentType(String type);

    boolean isSourceClosed();

    void setSourceClosed(boolean sc);

    boolean isMatch(Node node);

    Node getCloseNode();

    Node getOpenNode();

    void setCloseNode(Node node);

    void setOpenNode(Node node);
}
