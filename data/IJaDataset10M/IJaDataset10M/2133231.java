package com.aloaproject.ciquta;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Daniele Demichelis
 */
class CountProjection implements Projection {

    private class Status {

        long count = 0;

        Status inc() {
            count = count + 1;
            return this;
        }

        long result() {
            return count;
        }
    }

    CountProjection() {
    }

    public List getResult(Object projectionStatus) {
        List r = new ArrayList();
        r.add(new Long(((Status) projectionStatus).result()));
        return r;
    }

    public Object getStartStatus() {
        return new Status();
    }

    public Object project(Object projectionStatus, Object object) {
        return ((Status) projectionStatus).inc();
    }
}
