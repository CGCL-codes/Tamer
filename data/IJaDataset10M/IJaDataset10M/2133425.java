package com.amazonaws.eclipse.ec2.ui.views.instances;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import com.amazonaws.eclipse.ec2.Ec2Plugin;
import com.amazonaws.eclipse.ec2.keypairs.KeyPairManager;
import com.amazonaws.eclipse.ec2.ui.SetupAwsAccountAction;
import com.amazonaws.services.ec2.model.Instance;

final class CreateAmiAction extends Action {

    private final InstanceSelectionTable instanceSelectionTable;

    public CreateAmiAction(InstanceSelectionTable instanceSelectionTable) {
        this.instanceSelectionTable = instanceSelectionTable;
    }

    public void run() {
        for (Instance instance : instanceSelectionTable.getAllSelectedInstances()) {
            createAmiFromInstance(instance);
        }
    }

    private void createAmiFromInstance(Instance instance) {
        boolean userIdIsValid = (InstanceSelectionTable.accountInfo.getUserId() != null);
        if (!InstanceSelectionTable.accountInfo.isValid() || !InstanceSelectionTable.accountInfo.isCertificateValid() || !userIdIsValid) {
            String message = "Your AWS account information doesn't appear to be fully configured yet.  " + "To bundle an instance you'll need all the information configured, " + "including your AWS account ID, EC2 certificate and private key file." + "\n\nWould you like to configure it now?";
            if (MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), "Configure AWS Account Information", message)) {
                new SetupAwsAccountAction().run();
            }
            return;
        }
        KeyPairManager keyPairManager = new KeyPairManager();
        String keyName = instance.getKeyName();
        String keyPairFilePath = keyPairManager.lookupKeyPairPrivateKeyFile(keyName);
        if (keyPairFilePath == null) {
            String message = "There is no private key registered for the key this host was launched with (" + keyName + ").";
            MessageDialog.openError(Display.getCurrent().getActiveShell(), "No Registered Private Key", message);
            return;
        }
        BundleDialog bundleDialog = new BundleDialog(Display.getCurrent().getActiveShell());
        if (bundleDialog.open() != IDialogConstants.OK_ID) return;
        String bundleName = bundleDialog.getImageName();
        String s3Bucket = bundleDialog.getS3Bucket();
        BundleJob job = new BundleJob(instance, s3Bucket, bundleName);
        job.schedule();
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return Ec2Plugin.getDefault().getImageRegistry().getDescriptor("bundle");
    }

    @Override
    public String getText() {
        return "Bundle AMI...";
    }

    @Override
    public String getToolTipText() {
        return "Create a new Amazon Machine Image from this instance";
    }
}
