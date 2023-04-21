package dbsync4j.core.concrete;

import com.mythosis.beandiff.DiffField;
import com.mythosis.beandiff.Diffable;
import dbsync4j.core.behavior.NamedObject;

/**
 * Respons�vel por abstrair qualquer objeto no banco de dados relacional que possua uma indentifica��o �nica pelo nome.
 * 
 * @author Rafael
 *
 */
@Diffable
public abstract class AbstractNamedObject implements NamedObject {

    private String name;

    @DiffField
    private String remarks;

    public AbstractNamedObject(String name, String remarks) {
        this.name = name;
        this.remarks = remarks;
    }

    public String getName() {
        return name;
    }

    public String getRemarks() {
        return remarks;
    }
}
