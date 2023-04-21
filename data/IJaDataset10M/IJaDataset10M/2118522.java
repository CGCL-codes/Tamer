package net.sf.xplanner.dao.impl;

import net.sf.xplanner.dao.PersonDao;
import net.sf.xplanner.domain.Person;
import com.technoetic.xplanner.domain.repository.DuplicateUserIdException;

/**
 *    XplannerPlus, agile planning software
 *    @author Maksym_Chyrkov. 
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
public class PersonDaoImpl extends BaseDao<Person> implements PersonDao {

    @Override
    public int save(Person person) {
        Person uniqueObject = getUniqueObject(Person.USER_ID, person.getUserId());
        if (uniqueObject == null) {
            throw new DuplicateUserIdException();
        }
        return super.save(person);
    }
}
