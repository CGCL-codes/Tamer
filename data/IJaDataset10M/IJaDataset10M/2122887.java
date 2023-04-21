package uk.ac.shef.wit.text.chunker;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="mailto:z.zhang@dcs.shef.ac.uk">Ziqi Zhang</a>
 */
public interface EntityChunker extends Serializable {

    List<String> chunk(List<String> tokens) throws IOException;

    String getStartTag();

    String getContTag();

    String getEntityCategory();
}
