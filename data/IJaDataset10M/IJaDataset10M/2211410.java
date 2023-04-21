package net.sf.xplanner.events;

import org.springframework.context.ApplicationEvent;

/**
*    XplannerPlus, agile planning software
*    @author Maksym. 
*    Copyright (C) 2009  Maksym Chyrkov
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>
* 	 
*/
@XplannerEvent
public class ObjectDeleted extends ApplicationEvent {

    private static final long serialVersionUID = -1729312071033695919L;

    public ObjectDeleted(EventSource source) {
        super(source);
    }

    public EventSource getSource() {
        return (EventSource) super.getSource();
    }
}
