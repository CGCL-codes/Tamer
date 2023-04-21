package org.nakedobjects.xat.integ.junit4.injection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import org.nakedobjects.testlib.annotations.DocumentUsing;
import org.nakedobjects.testlib.documentor.Documentor;
import org.nakedobjects.xat.documentor.mem.InMemoryDocumentor;
import org.nakedobjects.xat.integ.junit4.NakedObjectsTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(NakedObjectsTestRunner.class)
@DocumentUsing(InMemoryDocumentor.class)
public class DocumentorInjected {

    private Documentor documentor;

    @Test
    public void shouldBeInjected() {
        assertThat(documentor, is(notNullValue()));
    }

    protected Documentor getDocumentor() {
        return documentor;
    }

    public void setDocumentor(final Documentor documentor) {
        this.documentor = documentor;
    }
}
