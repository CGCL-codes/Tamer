package com.demdex.idgen;

/**
 * An exception indicating a version number error when performing an update to an IDProvider.
 * 
 * This exception means that the copy of the data in memory is stale, and so can not be used perform an update to the IDProvider.
 * (i.e., the version number of the data in memory does not match the version number of the data in the IDProvider) 
 * 
 * @author D.Rosenstrauch, Demdex Inc.
 * $Revision: 7 $
 * $Date: 2011-05-19 02:09:35 -0400 (Thu, 19 May 2011) $
 * $LastChangedBy: darose $
 * 
 * ======
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *  http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
public class IDProviderVersionException extends IDGeneratorException {

    public IDProviderVersionException(String msg, IDCategory category, int expectedVersion, int actualVersion) {
        this(msg, category, expectedVersion, actualVersion, null);
    }

    public IDProviderVersionException(String msg, IDCategory category, int expectedVersion, int actualVersion, Exception cause) {
        super(msg + " on category: " + category.getName() + "; expected version: " + expectedVersion + ", actual version: " + actualVersion, cause);
        this.category = category;
        this.expectedVersion = expectedVersion;
        this.actualVersion = actualVersion;
    }

    public IDCategory getCategory() {
        return category;
    }

    public int getExpectedVersion() {
        return expectedVersion;
    }

    public int getActualVersion() {
        return actualVersion;
    }

    public static final int UNKNOWN = -1;

    private IDCategory category;

    private int expectedVersion = UNKNOWN;

    private int actualVersion = UNKNOWN;
}
