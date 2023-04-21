package org.easyrec.controller.clusterManager;

import com.google.common.base.Strings;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.Tree;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONStringer;
import org.codehaus.jettison.json.JSONWriter;
import org.easyrec.exception.core.ClusterException;
import org.easyrec.model.core.ClusterVO;
import org.easyrec.model.core.ItemAssocVO;
import org.easyrec.model.core.ItemVO;
import org.easyrec.model.web.RemoteTenant;
import org.easyrec.service.core.ClusterService;
import org.easyrec.service.web.IDMappingService;
import org.easyrec.service.web.ViewInitializationService;
import org.easyrec.store.dao.core.types.ItemTypeDAO;
import org.easyrec.store.dao.web.ItemDAO;
import org.easyrec.store.dao.web.RemoteTenantDAO;
import org.easyrec.utils.servlet.ServletUtils;
import org.easyrec.utils.spring.store.dao.IDMappingDAO;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * This Controller is used to create the basic functionality of the cluster manager
 * it handles create, displaying and deleting of clusters and adding and displaying items
 * of the cluster.
 *
 * @author dmann
 */
public class ClusterManagerController extends MultiActionController {

    private ClusterService clusterService;

    private RemoteTenantDAO remoteTenantDAO;

    private ItemDAO itemDAO;

    private ItemTypeDAO itemTypeDAO;

    private IDMappingDAO idMappingDAO;

    private IDMappingService idMappingService;

    private ViewInitializationService viewInitializationService;

    public ClusterManagerController(ClusterService clusterService, RemoteTenantDAO remoteTenantDAO, ItemDAO itemDAO, ItemTypeDAO itemTypeDAO, IDMappingDAO idMappingDAO, IDMappingService idMappingService, ViewInitializationService viewInitializationService) {
        this.clusterService = clusterService;
        this.remoteTenantDAO = remoteTenantDAO;
        this.itemDAO = itemDAO;
        this.itemTypeDAO = itemTypeDAO;
        this.idMappingDAO = idMappingDAO;
        this.idMappingService = idMappingService;
        this.viewInitializationService = viewInitializationService;
    }

    public ModelAndView clustermanager(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        ModelAndView mav = new ModelAndView("clustermanager/clustermanager");
        RemoteTenant remoteTenant = viewInitializationService.initializeView(request, mav);
        if (remoteTenant != null) {
            Set<String> itemTypes = itemTypeDAO.getTypes(remoteTenant.getId(), true);
            mav.addObject("availableItemTypes", itemTypes);
        }
        return mav;
    }

    public ModelAndView help(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        ModelAndView mav = new ModelAndView("clustermanager/help");
        RemoteTenant remoteTenant = viewInitializationService.initializeView(request, mav);
        return mav;
    }

    public ModelAndView viewitems(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        ModelAndView mav = new ModelAndView("clustermanager/viewitems");
        RemoteTenant remoteTenant = viewInitializationService.initializeView(request, mav);
        String clusterId = ServletUtils.getSafeParameter(request, "clusterId", "");
        ClusterVO cluster = clusterService.loadCluster(remoteTenant.getId(), clusterId);
        mav.addObject("cluster", cluster);
        return mav;
    }

    public ModelAndView additemtocluster(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        ModelAndView mav = new ModelAndView("clustermanager/ajax/message");
        RemoteTenant remoteTenant = viewInitializationService.initializeView(request, mav);
        String clusterId = ServletUtils.getSafeParameter(request, "clusterId", "");
        String itemId = ServletUtils.getSafeParameter(request, "itemId", "");
        String itemType = ServletUtils.getSafeParameter(request, "itemType", "");
        Integer itemTypeId = itemTypeDAO.getIdOfType(remoteTenant.getId(), itemType);
        Integer itemIdInt = idMappingDAO.lookup(itemId);
        try {
            clusterService.addItemToCluster(remoteTenant.getId(), clusterId, itemIdInt, itemTypeId);
        } catch (ClusterException e) {
            mav.addObject("text", e.getMessage());
            return mav;
        }
        return mav;
    }

    public ModelAndView removeitemfromcluster(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        ModelAndView mav = new ModelAndView("clustermanager/ajax/message");
        RemoteTenant remoteTenant = viewInitializationService.initializeView(request, mav);
        String clusterId = ServletUtils.getSafeParameter(request, "clusterId", "");
        String itemId = ServletUtils.getSafeParameter(request, "itemId", "");
        String itemType = ServletUtils.getSafeParameter(request, "itemType", "");
        Integer itemTypeId = itemTypeDAO.getIdOfType(remoteTenant.getId(), itemType);
        Integer itemIdInt = idMappingDAO.lookup(itemId);
        try {
            clusterService.removeItemFromCluster(remoteTenant.getId(), clusterId, itemIdInt, itemTypeId);
        } catch (ClusterException e) {
            mav.addObject("text", e.getMessage());
            return mav;
        }
        return mav;
    }

    public ModelAndView updatecluster(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        ModelAndView mav = new ModelAndView("clustermanager/ajax/message");
        RemoteTenant remoteTenant = viewInitializationService.initializeView(request, mav);
        String originalClusterId = ServletUtils.getSafeParameter(request, "originalClusterId", "");
        String clusterId = ServletUtils.getSafeParameter(request, "clusterId", "");
        String clusterDescription = ServletUtils.getSafeParameter(request, "clusterDescription", "");
        ClusterVO cluster = clusterService.loadCluster(remoteTenant.getId(), originalClusterId);
        cluster.setName(clusterId);
        cluster.setDescription(clusterDescription);
        try {
            clusterService.updateClusterDescription(remoteTenant.getId(), originalClusterId, clusterDescription);
        } catch (Exception e) {
            mav.addObject("text", e.getMessage());
            return mav;
        }
        if (!originalClusterId.equals(clusterId)) {
            try {
                clusterService.renameCluster(remoteTenant.getId(), originalClusterId, clusterId);
            } catch (Exception e) {
                mav.addObject("text", e.getMessage());
                return mav;
            }
        }
        return mav;
    }

    public ModelAndView clusteritemtable(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        ModelAndView mav = new ModelAndView("clustermanager/ajax/clusteritemtable");
        final RemoteTenant remoteTenant = viewInitializationService.initializeView(request, mav);
        String clusterId = ServletUtils.getSafeParameter(request, "clusterId", "");
        ClusterVO cluster = clusterService.loadCluster(remoteTenant.getId(), clusterId);
        List<ItemVO<Integer, Integer>> items = clusterService.getItemsOfCluster(cluster);
        mav.addObject("cluster", cluster);
        mav.addObject("items", idMappingService.mapListOfItemVOs(items, remoteTenant));
        return mav;
    }

    public ModelAndView changeclusterparent(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        ModelAndView mav = new ModelAndView("clustermanager/ajax/message");
        RemoteTenant remoteTenant = viewInitializationService.initializeView(request, mav);
        String clusterId = ServletUtils.getSafeParameter(request, "clusterId", "");
        String newParent = ServletUtils.getSafeParameter(request, "newParent", "");
        if (remoteTenant == null) {
            logger.warn("no tenantId supplied");
            return mav;
        }
        if (clusterId.length() == 0) {
            logger.warn("no clusterId supplied");
            return mav;
        }
        clusterService.moveCluster(remoteTenant.getId(), clusterId, newParent);
        return mav;
    }

    public ModelAndView createcluster(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        ModelAndView mav = new ModelAndView("clustermanager/ajax/message");
        RemoteTenant remoteTenant = viewInitializationService.initializeView(request, mav);
        String clusterId = ServletUtils.getSafeParameter(request, "clusterId", "");
        String parent = ServletUtils.getSafeParameter(request, "parent", "");
        if (clusterId.contains(" ")) {
            mav.addObject("text", "You cannot use spaces in cluster names, sorry.");
            return mav;
        }
        if (remoteTenant == null) {
            logger.warn("no tenantId supplied");
            return mav;
        }
        if (clusterId.length() == 0) {
            logger.warn("no clusterId supplied");
            return mav;
        }
        try {
            clusterService.addCluster(remoteTenant.getId(), clusterId, "", parent);
        } catch (ClusterException e) {
            logger.warn("error occurred when adding cluster", e);
            mav.addObject("text", e.getMessage());
            return mav;
        }
        return mav;
    }

    public ModelAndView deletecluster(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        ModelAndView mav = new ModelAndView("clustermanager/ajax/message");
        RemoteTenant remoteTenant = viewInitializationService.initializeView(request, mav);
        String clusterId = ServletUtils.getSafeParameter(request, "clusterId", "");
        String parent = ServletUtils.getSafeParameter(request, "parent", "");
        if (clusterId.equals("CLUSTERS")) {
            mav.addObject("text", "You cannot delete the main container.");
            return mav;
        }
        if (clusterId.equals("")) {
            mav.addObject("text", "No Cluster selected to delete.");
            return mav;
        }
        if (remoteTenant == null) {
            logger.warn("no tenantId supplied");
            return mav;
        }
        if (clusterId.length() == 0) {
            logger.warn("no clusterId supplied");
            return mav;
        }
        clusterService.removeCluster(remoteTenant.getId(), clusterId);
        return mav;
    }

    public ModelAndView loadtreedata(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        ModelAndView mav = new ModelAndView("clustermanager/ajax/loadtreedata");
        RemoteTenant remoteTenant = viewInitializationService.initializeView(request, mav);
        if (remoteTenant == null) {
            logger.warn("no tenantId supplied");
            return mav;
        }
        DelegateTree<ClusterVO, ItemAssocVO<Integer, Integer>> clusterTree = clusterService.getClustersForTenant(remoteTenant.getId());
        JsonTreeGraphWriter graphWriter = new JsonTreeGraphWriter();
        JSONWriter jsonWriter = new JSONStringer();
        try {
            graphWriter.save(clusterTree, jsonWriter);
        } catch (JSONException e) {
            logger.warn("failed to serialize cluster tree to JSON", e);
            return mav;
        }
        mav.addObject("treeJsonData", jsonWriter.toString());
        return mav;
    }

    /**
     * convert a JUNG tree to a format that can be handled by the jsonTree library
     *
     * @author pmarschik
     */
    private static class JsonTreeGraphWriter {

        public void save(Tree<ClusterVO, ItemAssocVO<Integer, Integer>> tree, JSONWriter writer) throws JSONException {
            ClusterVO root = tree.getRoot();
            writeNode(tree, root, writer, root);
        }

        private void writeNode(Tree<ClusterVO, ItemAssocVO<Integer, Integer>> tree, ClusterVO node, JSONWriter writer, ClusterVO root) throws JSONException {
            writer.object().key("data").value(node.getName()).key("attr").object().key("id").value(node.getName()).key("title").value(node.getDescription() == null ? node.getName() : node.getDescription());
            if (node == root) writer.key("rel").value("root");
            writer.endObject();
            if (!Strings.isNullOrEmpty(node.getDescription())) writer.key("description").value(node.getDescription());
            Collection<ClusterVO> children = tree.getChildren(node);
            if (children.size() > 0) {
                writer.key("children").array();
                for (ClusterVO child : children) {
                    writeNode(tree, child, writer, root);
                }
                writer.endArray();
            }
            writer.endObject();
        }
    }
}
