package br.gov.framework.demoiselle.web.message;

import static org.junit.Assert.assertEquals;
import java.util.Locale;
import org.junit.Before;
import org.junit.Test;
import br.gov.framework.demoiselle.core.message.IMessage;
import br.gov.framework.demoiselle.core.message.Severity;
import br.gov.framework.demoiselle.web.message.WebMessageContext;

public class WebMessageContextTest {

    private IMessage message;

    @Before
    public void setUp() throws Exception {
        message = new IMessage() {

            public String getKey() {
                return "ERROR_0001";
            }

            public String getLabel() {
                return "Error Message";
            }

            public Locale getLocale() {
                return Locale.US;
            }

            public String getResourceName() {
                return "error";
            }

            public Severity getSeverity() {
                return Severity.ERROR;
            }

            public String toString() {
                return this.getKey() + ":" + this.getLabel();
            }
        };
    }

    @Test
    public void testClear() {
        WebMessageContext.getInstance().addMessage(message);
        WebMessageContext.getInstance().clear();
        assertEquals(0, WebMessageContext.getInstance().getMessages().size());
    }

    @Test
    public void testGetMessages() {
        WebMessageContext.getInstance().addMessage(message);
        assertEquals(1, WebMessageContext.getInstance().getMessages().size());
    }
}
