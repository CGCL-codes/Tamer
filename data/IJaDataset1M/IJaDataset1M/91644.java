/*
 * one line to give the library's name and an idea of what it does.
 * Copyright (C) Curallucci de Peretti Emmanuel
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *  
*/
package adm.java.librairie;

import java.util.*;

public class LinkObjectSource {

	protected String link=null;
	protected Vector<Ihm> list = new Vector<Ihm>();
	
	public LinkObjectSource(){}
	public LinkObjectSource(String link){
		this.link = link;
	}
	
	public Boolean add(Ihm object){
		this.list.addElement(object);
		return true;
	}
	
	public String getLink(){
		return this.link;
	}	
	public Vector<Ihm> getObject(){
		return this.list;
	}	
}
