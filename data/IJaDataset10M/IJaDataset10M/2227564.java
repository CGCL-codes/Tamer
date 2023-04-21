package iwork.eheap2.logger;

import iwork.eheap2.*;
import java.io.*;
import java.util.*;

public interface EventLogMetaDataListener {

    public void newField(String fieldName, Class fieldClass);

    public void newEventType(String eventType);

    public void fieldVisibilityChanged(String fieldName, Class fieldClass, boolean visible);

    public void eventTypeVisibilityChanged(String eventType, boolean visible);
}
