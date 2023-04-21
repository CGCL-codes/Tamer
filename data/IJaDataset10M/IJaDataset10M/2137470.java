package com.demdex.idgen;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command-line utility for administering the Zookeeper-based IDGenerator data
 * 
 * @author D.Rosenstrauch, Demdex Inc.
 * $Revision: 7 $
 * $Date: 2011-05-19 02:09:35 -0400 (Thu, 19 May 2011) $
 * $LastChangedBy: darose $
 * 
 * ======
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *  http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
public class IDGeneratorCLI {

    public static void main(String[] args) throws Exception {
        checkUsageCommandIndependent(args, COMMANDS, HOST_LIST, CATEGORY);
        String command = args[0];
        String zkHostList = args[1];
        String category = args[2];
        IDGeneratorCLI cli = new IDGeneratorCLI(zkHostList);
        try {
            if (command.equalsIgnoreCase(CREATE)) {
                checkUsageCommandDependent(args, CREATE, HOST_LIST, CATEGORY, RANGE_STR);
                String rangeStr = args[3];
                cli.create(category, rangeStr);
            } else if (command.equalsIgnoreCase(READ)) {
                checkUsageCommandDependent(args, READ, HOST_LIST, CATEGORY);
                cli.read(category);
            } else if (command.equalsIgnoreCase(DELETE)) {
                checkUsageCommandDependent(args, DELETE, HOST_LIST, CATEGORY);
                cli.delete(category);
            } else if (command.equalsIgnoreCase(SET)) {
                checkUsageCommandDependent(args, SET, HOST_LIST, CATEGORY, RANGE_STR);
                String rangeStr = args[3];
                cli.set(category, rangeStr);
            } else if (command.equalsIgnoreCase(TAKE)) {
                checkUsageCommandDependent(args, TAKE, HOST_LIST, CATEGORY, "<# ID's to take>");
                int size = Integer.parseInt(args[3]);
                cli.take(category, size);
            } else if (command.equalsIgnoreCase(PUSH)) {
                checkUsageCommandDependent(args, PUSH, HOST_LIST, CATEGORY, RANGE_STR);
                String rangeStr = args[3];
                cli.push(category, rangeStr);
            } else {
                throw new IllegalArgumentException("Unknown command: " + command);
            }
        } finally {
            if (cli.isOpen()) {
                cli.close();
            }
        }
    }

    public IDGeneratorCLI(String zkHostList) {
        idProvider = new ZooKeeperIDProvider(zkHostList, DEFAULT_SESSION_TIMEOUT);
        idGenerator = new IDGenerator(idProvider);
        logger = LoggerFactory.getLogger(IDGeneratorCLI.class);
    }

    public void create(String category, String rangeStr) throws Exception {
        open();
        doCreate(new File(category), rangeStr);
        System.out.println("created");
    }

    public void delete(String category) throws Exception {
        open();
        zookeeper.delete(category, ANY_VERSION);
        System.out.println("deleted");
    }

    public void read(String category) throws Exception {
        open();
        IDSet idSet = idGenerator.peekIDs(new IDCategory(category));
        System.out.println("Read idSet: " + idSet.toString());
    }

    public void set(String category, String rangeStr) throws Exception {
        open();
        if (rangeStr.indexOf(COMMA) > 0) {
            rangeStr = rangeStr.replace(COMMA, '\n');
        }
        zookeeper.setData(category, rangeStr.getBytes(), ANY_VERSION);
        System.out.println("set");
    }

    public void take(String category, int size) throws Exception {
        open();
        IDSet idSet = idGenerator.takeIDs(new IDCategory(category), size);
        System.out.println("Took idSet: " + idSet.toString());
    }

    public void push(String category, String rangeStr) throws Exception {
        open();
        IDRange range = IDRange.parse(rangeStr);
        idGenerator.pushIDs(new IDSet(new IDCategory(category), range));
        System.out.println("pushed");
    }

    private void open() throws IDGeneratorException {
        idGenerator.open();
        zookeeper = idProvider.getZookeeper();
        open = true;
    }

    private boolean isOpen() {
        return open;
    }

    private void close() throws IDGeneratorException {
        idGenerator.close();
        open = false;
    }

    private void doCreate(File category, String rangeStr) throws KeeperException, InterruptedException {
        ensureParentCreated(category);
        zookeeper.create(category.getPath(), rangeStr.getBytes(), DEFAULT_ACL, CreateMode.PERSISTENT);
    }

    private void ensureParentCreated(File category) throws KeeperException, InterruptedException {
        File parent = category.getParentFile();
        Stat parentStat = zookeeper.exists(parent.getPath(), false);
        if (parentStat != null) {
            return;
        }
        doCreate(parent, "");
    }

    private static void checkUsageCommandIndependent(String[] args, String... expectedArgs) {
        boolean displayUsage = doCheckUsage(args, expectedArgs);
        if (!displayUsage) {
            return;
        }
        System.out.println(" <command-specific args>...");
        System.exit(1);
    }

    private static void checkUsageCommandDependent(String[] args, String... expectedArgs) {
        boolean displayUsage = doCheckUsage(args, expectedArgs);
        if (!displayUsage) {
            return;
        }
        System.out.println();
        System.exit(1);
    }

    private static boolean doCheckUsage(String[] args, String... expectedArgs) {
        if (args.length >= expectedArgs.length) {
            return false;
        }
        System.out.print("Usage:\tIDGeneratorCLI");
        for (String expectedArg : expectedArgs) {
            System.out.print(' ');
            System.out.print(expectedArg);
        }
        return true;
    }

    private static final String CREATE = "create";

    private static final String READ = "read";

    private static final String SET = "set";

    private static final String DELETE = "delete";

    private static final String TAKE = "take";

    private static final String PUSH = "push";

    private static final String[] ALL_COMMANDS = { CREATE, READ, SET, DELETE, TAKE, PUSH };

    private static final String COMMANDS = "<Command (one of: " + Arrays.toString(ALL_COMMANDS) + ")>";

    private static final String HOST_LIST = "<ZooKeeper hostname list>";

    private static final String CATEGORY = "<ZooKeeper node path>";

    private static final String RANGE_STR = "<Range (i.e., \"startID-endID\")>";

    private static final int DEFAULT_SESSION_TIMEOUT = 10000;

    private static final int ANY_VERSION = -1;

    private static final List<ACL> DEFAULT_ACL = ZooDefs.Ids.OPEN_ACL_UNSAFE;

    private static final char COMMA = ',';

    private ZooKeeperIDProvider idProvider;

    private IDGenerator idGenerator;

    private ZooKeeper zookeeper;

    private Logger logger;

    private boolean open;
}
