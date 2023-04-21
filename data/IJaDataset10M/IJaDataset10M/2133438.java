package com.webobjects.woextensions;

import com.webobjects._ideservices._IDEProject;
import com.webobjects._ideservices._WOProject;
import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WODirectAction;
import com.webobjects.appserver.WORequest;
import com.webobjects.appserver.WOResourceManager;
import com.webobjects.appserver.WOResponse;
import com.webobjects.appserver._private.WODeployedBundle;
import com.webobjects.appserver._private.WOProjectBundle;
import com.webobjects.foundation.NSNumberFormatter;

public class WOProjectBuilderAction extends WODirectAction {

    public WOProjectBuilderAction(WORequest aRequest) {
        super(aRequest);
    }

    protected WOResponse javascriptBack() {
        WOResponse response = WOApplication.application().createResponseInContext(null);
        response.appendContentString("<HTML><BODY><SCRIPT>history.go(-1);</SCRIPT><P>Please use the <B>back</B> button of your browser to go back to the Exception page.</P></BODY></HTML>");
        return response;
    }

    public WOActionResults openInProjectBuilderAction() {
        WORequest request = request();
        String filename, errorMessage, fullClassName;
        Number line;
        line = request.numericFormValueForKey("line", new NSNumberFormatter("#0"));
        filename = (String) request.stringFormValueForKey("filename");
        errorMessage = (String) request.stringFormValueForKey("errorMessage");
        fullClassName = (String) request.stringFormValueForKey("fullClassName");
        WOResourceManager resources = WOApplication.application().resourceManager();
        WODeployedBundle appBundle = resources._appProjectBundle();
        if (appBundle instanceof WOProjectBundle) {
            WOProjectBundle project = (WOProjectBundle) appBundle;
            _WOProject woproject = project._woProject();
            String filePath = woproject._pathToSourceFileForClass(fullClassName, filename);
            if (filePath == null) {
            } else {
                _IDEProject ideproject = woproject.ideProject();
                int lineInt = (line == null) ? 0 : line.intValue();
                ideproject.openFile(filePath, lineInt, errorMessage);
            }
        }
        return javascriptBack();
    }
}
