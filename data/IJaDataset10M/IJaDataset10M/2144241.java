package clear.demo.jpa;

import java.util.Collection;
import java.util.List;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import clear.data.AbstractAssembler;
import clear.demo.jpa.session.DataBean;

public class Assembler extends AbstractAssembler {

    DataBean databean;

    public Assembler() {
        InitialContext ctx;
        try {
            ctx = new InitialContext();
            databean = (DataBean) ctx.lookup("flex/DataBean/no-interface");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public Collection fill(List fp) {
        return databean.fill(fp);
    }

    public List sync(List changes) {
        return databean.sync(changes);
    }

    public List executeTransactionBatch(List batch) throws Throwable {
        return databean.executeTransactionBatch(batch);
    }
}
