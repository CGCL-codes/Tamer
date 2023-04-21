package org.dspace.sword2;

import org.dspace.content.Bitstream;
import org.dspace.content.Bundle;
import org.dspace.content.Collection;
import org.dspace.content.Item;
import org.dspace.core.Context;
import org.swordapp.server.Deposit;
import org.swordapp.server.SwordError;
import org.swordapp.server.UriRegistry;
import java.sql.SQLException;

public class WorkflowManagerDefault implements WorkflowManager {

    public void retrieveServiceDoc(Context context) throws SwordError {
    }

    public void listCollectionContents(Context context, Collection collection) throws SwordError {
    }

    public void createResource(Context context, Collection collection) throws SwordError {
    }

    public void retrieveContent(Context context, Item item) throws SwordError {
    }

    public void retrieveBitstream(Context context, Bitstream bitstream) throws SwordError, DSpaceSwordException {
    }

    public void replaceResourceContent(Context context, Item item) throws SwordError, DSpaceSwordException {
        WorkflowTools wft = new WorkflowTools();
        if (item.isArchived() || item.isWithdrawn()) {
            throw new SwordError(UriRegistry.ERROR_METHOD_NOT_ALLOWED, "The item has already been archived, and can no longer be modified");
        }
        if (wft.isItemInWorkflow(context, item)) {
            throw new SwordError(UriRegistry.ERROR_METHOD_NOT_ALLOWED, "The item has already been injected into the review workflow, and can no longer be modified");
        }
    }

    public void replaceMetadata(Context context, Item item) throws SwordError, DSpaceSwordException {
        WorkflowTools wft = new WorkflowTools();
        if (item.isArchived() || item.isWithdrawn()) {
            throw new SwordError(UriRegistry.ERROR_METHOD_NOT_ALLOWED, "The item has already been archived, and can no longer be modified");
        }
        if (wft.isItemInWorkflow(context, item)) {
            throw new SwordError(UriRegistry.ERROR_METHOD_NOT_ALLOWED, "The item has already been injected into the review workflow, and can no longer be modified");
        }
    }

    public void replaceMetadataAndMediaResource(Context context, Item item) throws SwordError, DSpaceSwordException {
        this.replaceResourceContent(context, item);
        this.replaceMetadata(context, item);
    }

    public void deleteMediaResource(Context context, Item item) throws SwordError, DSpaceSwordException {
        WorkflowTools wft = new WorkflowTools();
        if (item.isArchived() || item.isWithdrawn()) {
            throw new SwordError(UriRegistry.ERROR_METHOD_NOT_ALLOWED, "The item has already been archived, and can no longer be modified");
        }
        if (wft.isItemInWorkflow(context, item)) {
            throw new SwordError(UriRegistry.ERROR_METHOD_NOT_ALLOWED, "The item has already been injected into the review workflow, and can no longer be modified");
        }
    }

    public void deleteBitstream(Context context, Bitstream bitstream) throws SwordError, DSpaceSwordException {
        try {
            for (Bundle bundle : bitstream.getBundles()) {
                if (!"ORIGINAL".equals(bundle.getName())) {
                    throw new SwordError(UriRegistry.ERROR_METHOD_NOT_ALLOWED, "The file is not in a bundle which can be modified");
                }
                for (Item item : bundle.getItems()) {
                    this.deleteMediaResource(context, item);
                }
            }
        } catch (SQLException e) {
            throw new DSpaceSwordException(e);
        }
    }

    public void replaceBitstream(Context context, Bitstream bitstream) throws SwordError, DSpaceSwordException {
        try {
            for (Bundle bundle : bitstream.getBundles()) {
                if (!"ORIGINAL".equals(bundle.getName())) {
                    throw new SwordError(UriRegistry.ERROR_METHOD_NOT_ALLOWED, "The file is not in a bundle which can be modified");
                }
                for (Item item : bundle.getItems()) {
                    this.replaceResourceContent(context, item);
                }
            }
        } catch (SQLException e) {
            throw new DSpaceSwordException(e);
        }
    }

    public void addResourceContent(Context context, Item item) throws SwordError, DSpaceSwordException {
        WorkflowTools wft = new WorkflowTools();
        if (item.isArchived() || item.isWithdrawn()) {
            throw new SwordError(UriRegistry.ERROR_METHOD_NOT_ALLOWED, "The item has already been archived, and can no longer be modified");
        }
        if (wft.isItemInWorkflow(context, item)) {
            throw new SwordError(UriRegistry.ERROR_METHOD_NOT_ALLOWED, "The item has already been injected into the review workflow, and can no longer be modified");
        }
    }

    public void addMetadata(Context context, Item item) throws SwordError, DSpaceSwordException {
        WorkflowTools wft = new WorkflowTools();
        if (item.isArchived() || item.isWithdrawn()) {
            throw new SwordError(UriRegistry.ERROR_METHOD_NOT_ALLOWED, "The item has already been archived, and can no longer be modified");
        }
        if (wft.isItemInWorkflow(context, item)) {
            throw new SwordError(UriRegistry.ERROR_METHOD_NOT_ALLOWED, "The item has already been injected into the review workflow, and can no longer be modified");
        }
    }

    public void deleteItem(Context context, Item item) throws SwordError, DSpaceSwordException {
        WorkflowTools wft = new WorkflowTools();
        if (item.isArchived() || item.isWithdrawn()) {
            throw new SwordError(UriRegistry.ERROR_METHOD_NOT_ALLOWED, "The item has already been archived, and can no longer be modified");
        }
        if (wft.isItemInWorkflow(context, item)) {
            throw new SwordError(UriRegistry.ERROR_METHOD_NOT_ALLOWED, "The item has already been injected into the review workflow, and can no longer be modified");
        }
    }

    public void retrieveStatement(Context context, Item item) throws SwordError, DSpaceSwordException {
    }

    public void modifyState(Context context, Item item) throws SwordError, DSpaceSwordException {
        WorkflowTools wft = new WorkflowTools();
        if (item.isArchived() || item.isWithdrawn()) {
            throw new SwordError(UriRegistry.ERROR_METHOD_NOT_ALLOWED, "The item has already been archived, and can no longer be modified");
        }
        if (wft.isItemInWorkflow(context, item)) {
            throw new SwordError(UriRegistry.ERROR_METHOD_NOT_ALLOWED, "The item has already been injected into the review workflow, and can no longer be modified");
        }
    }

    public void resolveState(Context context, Deposit deposit, DepositResult result, VerboseDescription verboseDescription) throws DSpaceSwordException {
        this.resolveState(context, deposit, result, verboseDescription, true);
    }

    public void resolveState(Context context, Deposit deposit, DepositResult result, VerboseDescription verboseDescription, boolean containerOperation) throws DSpaceSwordException {
        if (!containerOperation) {
            return;
        }
        Item item = result.getItem();
        WorkflowTools wft = new WorkflowTools();
        boolean inwf = wft.isItemInWorkflow(context, item);
        boolean inws = wft.isItemInWorkspace(context, item);
        boolean inarch = item.isArchived() || item.isWithdrawn();
        if (!deposit.isInProgress() && inarch) {
            verboseDescription.append("The deposit is finished, but the item is already in the archive");
            throw new DSpaceSwordException("Invalid workflow state");
        }
        if (!deposit.isInProgress() && inws) {
            verboseDescription.append("The deposit is finished: moving it from the workspace to the workflow");
            wft.startWorkflow(context, item);
        }
        if (deposit.isInProgress() && inarch) {
            verboseDescription.append("The deposit is not finished, but the item is already in the archive");
            throw new DSpaceSwordException("Invalid workflow state");
        }
        if (deposit.isInProgress() && inwf) {
            verboseDescription.append("The deposit is in progress, but is in the workflow; returning to the workspace");
            wft.stopWorkflow(context, item);
        }
    }
}
