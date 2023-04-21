package com.inature.oce.persistence.model;

import java.sql.Timestamp;

/**
 * Copyright 2007 i-nature
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * 
 * @author Yavor Mitkov Gologanov
 *
 */
public class DocumentDTOE extends DocumentDTO implements Comparable<DocumentDTOE> {

    public static final long serialVersionUID = 1282910365133728737L;

    private String authorName = null;

    private Timestamp createTime = null;

    private Timestamp updateTime = null;

    /**
	 * 
	 */
    public int compareTo(DocumentDTOE obj) {
        return getTitle().compareTo(obj.getTitle());
    }

    /**
	 * 
	 * @return
	 */
    public String getAuthorName() {
        return authorName;
    }

    /**
	 * 
	 * @param authorName
	 */
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    /**
	 * 
	 * @return
	 */
    public Timestamp getCreateTime() {
        return createTime;
    }

    /**
	 * 
	 * @param createTime
	 */
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    /**
	 * 
	 * @return
	 */
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    /**
	 * 
	 * @param updateTime
	 */
    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
