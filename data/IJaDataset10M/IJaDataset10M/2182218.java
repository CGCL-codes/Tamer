package com.powerhua.privilege.logic;

import java.util.List;
import com.powerhua.privilege.domain.Operation;

public interface OperationManager {

    void create(Operation op);

    void update(Operation op);

    void delete(Operation op);

    List getOperations(Operation op, int start, int offset);
}
