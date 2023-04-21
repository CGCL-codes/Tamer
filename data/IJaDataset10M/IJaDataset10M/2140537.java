package org.xactor.test.recover.test;

import junit.framework.Test;

/**
 * A RecoveryFromCrashOfSecondRemoteResourceBeforeItAnswersPrepareSOAPJBRemTestCase.
 * 
 * @author <a href="reverbel@ime.usp.br">Francisco Reverbel</a>
 * @author <a href="mailto:ivanneto@gmail.com">Ivan Neto</a>
 * @version $Revision: 37406 $
 */
public class RecoveryFromCrashOfSecondRemoteResourceBeforeItAnswersPrepareSOAPJBRemTestCase extends RecoveryFromCrashOfSecondRemoteResourceBeforeItAnswersPrepareTestCase {

    public RecoveryFromCrashOfSecondRemoteResourceBeforeItAnswersPrepareSOAPJBRemTestCase(String name) {
        super(name);
    }

    public static Test suite() throws Exception {
        return suite(RecoveryFromCrashOfSecondRemoteResourceBeforeItAnswersPrepareSOAPJBRemTestCase.class);
    }
}
