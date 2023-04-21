package org.ourgrid.worker.controller;

import br.edu.ufcg.lsd.commune.identification.DeploymentID;

public class WorkerManagementControllerMessages {

    public static String getUnknownPeerSetPeerMessage(DeploymentID workerManagClientID, String senderPubKey) {
        return "The unknown peer [" + workerManagClientID + "] tried to set " + "itself as manager of this Worker. This message was ignored. " + "Unknown peer public key: [" + senderPubKey + "].";
    }

    public static String getPeerSetPeerMessage(DeploymentID peerID) {
        return "The peer [" + peerID + "] set itself as manager of this Worker.";
    }

    public static String getUnknownPeerTryingToCommandWorkerToWorkForMyGridMessage(String senderPubKey) {
        return "An unknown peer tried to command this Worker to work for a local consumer." + " This message was ignored. Unknown peer public key: [" + senderPubKey + "].";
    }

    public static String getMasterPeerTryingToCommandWorkerBeforeSettingAsManagerMessage() {
        return "The master Peer tried to manage this Worker before setting itself as manager of this Worker." + " This message was ignored.";
    }

    public static String getMasterPeerCommandedOwnerWorkerToWorkForMyGridMessage() {
        return "This Worker was commanded to work for a local consumer," + " but it is in the OWNER status. This message was ignored.";
    }

    public static String getMasterPeerCommandedWorkerToWorkForMyGridMessage(DeploymentID myGridID) {
        return "Peer commanded this Worker to work for a local consumer." + " Local consumer public key: [" + myGridID + "].";
    }

    public static String getUnknownPeerSendsWorkForPeerMessage(String senderPubKey) {
        return "An unknown peer tried to command this Worker to work for a remote peer. " + "This message was ignored. Unknown peer public key: [" + senderPubKey + "].";
    }

    public static String getUnsetMasterPeerSendsWorkForPeerMessage() {
        return "The master Peer tried to manage this Worker before setting itself as manager " + "of this Worker. This message was ignored.";
    }

    public static String getWorkForPeerOnOwnerWorkerMessage() {
        return "This Worker was commanded to work for a remote peer, " + "but it is in the OWNER status. This message was ignored.";
    }

    public static String getSuccessfulWorkForPeerMessage(String remotePeerPubKey) {
        return "Peer commanded this Worker to work for a remote peer. " + "Remote peer public key: [" + remotePeerPubKey + "].";
    }

    public static String getWorkForPeerOnAllocatedForMyGridWorkerMessage() {
        return "Strange behavior: This Worker was allocated to a local consumer, " + "but was now commanded to work for a remote peer.";
    }

    public static String getStopWorkingByUnknownPeerMessage(String senderPubKey) {
        return "An unknown peer tried to command this Worker to stop working. " + "This message was ignored. Unknown peer public key: [" + senderPubKey + "].";
    }

    public static String getStopWorkingOnNotWorkingWorkerMessage() {
        return "This Worker was commanded to stop working, but it's not working for any client. " + "This message was ignored.";
    }

    public static String getSuccessfulStopWorkingMessage() {
        return "This Worker was commanded to stop working for the current client.";
    }

    public static String getMasterPeerThatDidNotFailTryToSetPeerAgain(DeploymentID workerManagClientID) {
        return "The peer [" + workerManagClientID + "] set itself as manager of this Worker. This message was ignored." + " Because the master peer did not notify fail.";
    }
}
