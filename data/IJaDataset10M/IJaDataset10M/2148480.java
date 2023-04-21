package ggc.plugin.util;

import com.atech.i18n.I18nControlRunner;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Meter Tool (support for Meter devices)
 *
 *  See AUTHORS for copyright information.
 * 
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 * 
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 *  Filename:     GGCMeterICRunner  
 *  Description:  Definition for Language translation framework
 * 
 *  Author: Andy {andy@atech-software.com}
 */
public class GGCPluginICRunner extends I18nControlRunner {

    /**
     * Get Name of root file for application translation 
     * 
     * @return
     */
    @Override
    public String getLanguageFileRoot() {
        return "GGCPlugin";
    }
}
