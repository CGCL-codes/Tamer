package org.sodeja.rm;

import java.util.List;

public interface Operation {

    Object invoke(List<Object> objects);
}
