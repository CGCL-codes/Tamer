package cn.vlabs.duckling.vwb.services.share.votree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.log4j.Logger;
import cn.ac.ntarl.umt.api.ServiceContext;
import cn.ac.ntarl.umt.api.ServiceException;
import cn.ac.ntarl.umt.api.simpleAuth.VOTreeService;
import cn.ac.ntarl.umt.tree.AbstractNode;
import cn.ac.ntarl.umt.tree.GroupNode;
import cn.ac.ntarl.umt.tree.GroupTree;
import cn.ac.ntarl.umt.tree.Node;
import cn.vlabs.duckling.vwb.VWBContext;
import cn.vlabs.duckling.vwb.services.AbstractService;

/**
 * Introduction Here.
 * @date Mar 2, 2010
 * @author wkm(wkm@cnic.cn)
 */
public class VOTreeUtil extends AbstractService {

    private static final Logger log = Logger.getLogger(VOTreeUtil.class);

    private GroupTree voTree;

    private ArrayList<String[]> selectEntities;

    private ArrayList<String> selectEmails;

    private VWBContext context;

    private String vo;

    public VOTreeUtil(VWBContext context) {
        this.voTree = null;
        this.context = context;
        this.selectEntities = new ArrayList<String[]>();
        this.selectEmails = new ArrayList<String>();
        getVOTree();
    }

    public ArrayList<String> getSelectedEmail(String checked) {
        HashMap<String, String> check = new HashMap<String, String>();
        if (checked != null) {
            String[] values = checked.split(",");
            for (String temp : values) {
                if ((temp != null) && !(temp.trim().equals(""))) check.put(temp, "1");
            }
        }
        compareTree(this.voTree, check);
        ArrayList<String> tmpUsers = (ArrayList<String>) this.selectEmails.clone();
        this.selectEmails.clear();
        for (String tmp : tmpUsers) {
            boolean find = false;
            for (String tmp1 : this.selectEmails) {
                if (tmp.equals(tmp1)) {
                    find = true;
                    break;
                }
            }
            if (!find) this.selectEmails.add(tmp);
        }
        return this.selectEmails;
    }

    public ArrayList<String[]> getSelectedEntity(String checked) {
        HashMap<String, String> check = new HashMap<String, String>();
        if (checked != null) {
            String[] values = checked.split(",");
            for (String temp : values) {
                if ((temp != null) && !(temp.trim().equals(""))) check.put(temp, "1");
            }
        }
        compareTree(this.voTree, check);
        ArrayList<String[]> tmpEntities = (ArrayList<String[]>) this.selectEntities.clone();
        this.selectEntities.clear();
        for (String[] tmp : tmpEntities) {
            boolean find = false;
            for (String[] tmp1 : this.selectEntities) {
                if ((tmp[0].equals(tmp1[0])) && (tmp[1].equals(tmp1[1]))) {
                    find = true;
                    break;
                }
            }
            if (!find) this.selectEntities.add(tmp);
        }
        return this.selectEntities;
    }

    public void compareTree(GroupTree voTreeTemp, HashMap<String, String> checked) {
        Iterator<String> iter = checked.keySet().iterator();
        Node node;
        while (iter.hasNext()) {
            String id = iter.next();
            if ((node = voTreeTemp.getGroupByName(id)) != null) {
                String[] group = new String[2];
                group[1] = node.getName();
                group[0] = "group";
                selectEntities.add(group);
                if (node != null && node.hasChild()) {
                    Collection<Node> nodes = node.getChildren();
                    for (Node childnode : nodes) {
                        if (childnode.getNodeType().equals(AbstractNode.NODE_USER)) {
                            selectEmails.add(childnode.getName());
                        }
                    }
                }
            } else if ((node = voTreeTemp.getPositionByName(id)) != null) {
                String[] group = new String[2];
                group[1] = node.getName();
                group[0] = "group";
                selectEntities.add(group);
                if (node != null && node.hasChild()) {
                    Collection<Node> nodes = node.getChildren();
                    for (Node childnode : nodes) {
                        if (childnode.getNodeType().equals(AbstractNode.NODE_USER)) {
                            selectEmails.add(childnode.getName());
                        }
                    }
                }
            } else if ((node = voTreeTemp.getUserNodeByName(id)) != null) {
                String[] group = new String[2];
                group[1] = node.getName();
                group[0] = "user";
                selectEntities.add(group);
                selectEmails.add(node.getName());
            }
        }
    }

    public ArrayList<String[]> getEntities() {
        return this.selectEntities;
    }

    public ArrayList<String> getEmails() {
        return this.selectEmails;
    }

    private void getVOTree() {
        ServiceContext service = Utility.getUMTServiceContext(this.context);
        System.out.print("service值为：" + service);
        if ((vo == null) || (vo.equals("")) || (vo.equals("VO"))) {
            Utility utility = new Utility();
            vo = utility.getGroup(context);
            System.out.println("vo值为" + vo);
        }
        VOTreeService treeService = new VOTreeService(service);
        try {
            this.voTree = treeService.getTree("VO");
        } catch (ServiceException e) {
            e.printStackTrace();
            log.error("Get VOTree of " + vo + " error!");
        }
    }

    private String scriptHead, scriptBody, scriptFoot;

    public String generateScripts(HashMap<String, String> checked) {
        getVOTree();
        initScript();
        generateTree(checked);
        return getScripts();
    }

    private void initScript() {
        scriptHead = "<script language='JavaScript'>" + " var parent = null ;" + " var child = null ;" + " var grandparent = null ; var tree = null; var sub=null; var a=new Array(100); var layer=0;\n";
        scriptBody = "";
        scriptFoot = "document.write(tree);" + "</script>";
    }

    private void generateTree(HashMap<String, String> checked) {
        if (voTree != null) {
            while (vo.contains("/")) vo = vo.substring(vo.indexOf("/") + 1);
            Node node = voTree.getGroupByName(vo);
            String voId = ((GroupNode) node).getGroup().getName();
            String voName = ((GroupNode) node).getGroup().getDescription();
            VOTreeJSDumper dumper = new VOTreeJSDumper(voId, voName);
            dumper.setChecked(checked);
            dumper.visitChildren(node);
            scriptBody += dumper.toString();
        }
    }

    private String getScripts() {
        return scriptHead + scriptBody + scriptFoot;
    }
}
