package org.handwerkszeug.container.event;

import org.handwerkszeug.container.Nameable;

public interface Event extends Nameable {

    Context context();
}
