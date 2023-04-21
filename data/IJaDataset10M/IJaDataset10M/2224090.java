package com.neurogrid.database;

import java.util.*;
import com.neurogrid.middle.*;
import com.neurogrid.om.*;
import org.apache.log4j.Category;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

public interface PersistenceAction {

    public void persist();
}
