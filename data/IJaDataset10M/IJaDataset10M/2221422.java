package pl.matt.media.extractor;

import java.io.IOException;
import java.util.List;
import pl.matt.model.Rectangle;

public interface Extractor {

    public List<Rectangle> extract() throws IOException;
}
