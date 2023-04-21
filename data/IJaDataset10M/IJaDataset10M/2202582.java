package com.c2b2.ipoint.management;

/**
  * The MBean class that keeps track of access statistics.<br>
  * There is one instance of this MBean for each type of entity that has been accessed in 
  * the system
  * <p>
  * $Date: 2005/12/26 21:09:51 $
  * 
  * $Id: AccessStatistics.java,v 1.1 2005/12/26 21:09:51 steve Exp $<br>
  * 
  * Copyright 2005 C2B2 Consulting Limited. All rights reserved.
  * Use of this code is subject to license.
  * Please refer to your license agreement for terms and conditions.
  * </p>
  * @author $Author: steve $
  * @version $Revision: 1.1 $
  */
public class AccessStatistics implements AccessStatisticsMBean {

    private long myAccessCount;

    private long myCacheHits;

    private long myMaxTime;

    private long myMinTime;

    private long myTotalTime;

    private String myCommonName;

    public AccessStatistics() {
        myMinTime = Long.MAX_VALUE;
    }

    public long getPageAccessCount() {
        return myAccessCount;
    }

    public void accessed(long accessTime, boolean cached) {
        myAccessCount++;
        if (cached) myCacheHits++;
        if (accessTime < myMinTime) {
            myMinTime = accessTime;
        } else if (accessTime > myMaxTime) {
            myMaxTime = accessTime;
        }
        myTotalTime += accessTime;
        if (myMaxTime < myMinTime) {
            myMaxTime = myMinTime;
        }
    }

    public long getAverageAccessTime() {
        return myTotalTime / myAccessCount;
    }

    public long getMinAccessTime() {
        return myMinTime;
    }

    public long getMaxAccessTime() {
        return myMaxTime;
    }

    public long getCacheHits() {
        return myCacheHits;
    }
}
