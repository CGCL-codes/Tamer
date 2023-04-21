package org.unicase.kinectmssdkeclipse.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.AbstractHandler;
import org.unicase.kinectmssdkeclipse.KinectConnection;

public class GestureRecognitionHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        KinectConnection.getInstance().startGestureRecognition();
        return null;
    }
}
