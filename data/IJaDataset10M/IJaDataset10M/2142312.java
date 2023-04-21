package com.intel.gpe.client2.portalclient.portlets.tree;

import java.util.ArrayList;
import java.util.List;
import com.intel.gpe.client2.common.clientwrapper.ClientWrapper;
import com.intel.gpe.clients.api.GridFile;
import com.intel.gpe.clients.api.RegistryClient;
import com.intel.gpe.clients.api.StorageClient;
import com.intel.gpe.clients.api.TargetSystemClient;
import com.intel.gpe.clients.api.exceptions.GPEException;
import com.intel.gpe.gridbeans.web.webcontrols2.tree.Node;

/**
 * The node representing a storage
 * 
 * @author Alexander Lukichev
 * @version $Id: StorageNode.java,v 1.1 2006/10/23 13:55:54 lukichev Exp $
 *
 */
public class StorageNode extends Node {

    private ClientWrapper<RegistryClient, String> registry;

    private ClientWrapper<TargetSystemClient, String> targetSystem;

    private ClientWrapper<StorageClient, String> client;

    public StorageNode(Node parent, ClientWrapper<RegistryClient, String> registry, ClientWrapper<TargetSystemClient, String> targetSystem, ClientWrapper<StorageClient, String> client, String key) {
        super(parent, key);
        this.registry = registry;
        this.targetSystem = targetSystem;
        this.client = client;
    }

    @Override
    public List<Node> obtainChildren() {
        List<GridFile> files = null;
        try {
            files = client.getClient().listDirectory(".");
        } catch (GPEException e) {
            e.printStackTrace();
        }
        List<Node> children = new ArrayList<Node>();
        if (files != null) {
            int i = 0;
            for (GridFile file : files) {
                String name = file.getPath();
                if (name.startsWith("./")) {
                    name = name.substring(2);
                }
                children.add(new FileNode(this, registry, targetSystem, client, file, name, Integer.toString(i++)));
            }
        }
        return children;
    }

    @Override
    public String getIconPath() {
        return "/img/storage.gif";
    }

    @Override
    public String getText() {
        return client.getCache();
    }

    @Override
    public boolean isExpandable() {
        return true;
    }
}
