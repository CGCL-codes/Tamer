package com.nyandu.weboffice.common.config;

import com.nyandu.weboffice.common.config.properties.ConfException;

/**
 * 
 *  The contents of this file are subject to the Nandu Public License
 * Version 1.1 ("License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.nyandu.com
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and limitations
 * under the License.
 * 
 * The Initial Developer of the Original Code is User.
 * Portions created by User are Copyleft (C) www.nyandu.com. 
 * All Rights Reserved.
 * 
 * Contributor(s): ______________________________________.
 * User: fernando
 * Date: 17/02/2005
 * Time: 20:59:48
 */
public interface IParam {

    public void load() throws ConfException;

    public String get(String jic, String ac, String file);
}
