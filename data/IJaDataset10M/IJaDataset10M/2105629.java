package org.jqc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;
import org.jqc.QcCustomization.TABLE_NAME;
import org.qctools4j.exception.QcException;
import org.qctools4j.model.permission.FieldDescription;

/**
 * TODO Insert class description
 *
 * @author usf02000
 *
 */
public class TestWriteData extends AbstractTester {

    @Override
    protected String getProject() {
        return "Briut_Chitum";
    }

    public void writeData() throws QcException, Exception {
        testImpl(new QcConnectionEventsImpl<Object, Exception>() {

            @Override
            public Object loggedIn(final QcLoggedSession loggedSession) throws QcException, Exception {
                return super.loggedIn(loggedSession);
            }

            @Override
            public QcSessionResult<Object> connectedToPorject(final QcProjectConnectedSession qcProjectConnectedSession) throws QcException {
                try {
                    final FileOutputStream fw = new FileOutputStream(new File(getProject() + ".xml"));
                    final OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fw, "UTF-8");
                    final Map<String, FieldDescription> fieldDescriptions = qcProjectConnectedSession.getFieldDescriptions(TABLE_NAME.BUG);
                    outputStreamWriter.flush();
                    outputStreamWriter.close();
                    fw.flush();
                    fw.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                return QcSessionResult.getInstance();
            }
        });
    }

    public void testRead() {
        try {
            final FileInputStream fw = new FileInputStream(new File(getProject() + ".xml"));
            fw.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
