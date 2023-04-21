package javadata.data;

/**
 * <p>
 * <b>Title: </b>Factory class for {@link javadata.data.Data} objects.
 * </p>
 *
 * <p>
 * <b>Description: </b>Factory class for {@link javadata.data.Data} objects.
 * </p>
 * 
 * <p><b>Version: </b>1.0</p>
 * 
 * <p>
 * <b>Author: </b> Matthew Pearson, Copyright 2006, 2007
 * </p>
 * 
 * <p>
 * <b>License: </b>This file is part of JavaData.
 * </p>
 * <p>
 * JavaData is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * </p>
 * <p>
 * JavaData is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * </p>
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with JavaData.  If not, see 
 * <a href="http://www.gnu.org/licenses/">GNU licenses</a>.
 * </p> 
 * 
 */
public class DataFactory {

    /**
	 * Factory method for <code>Data</code> object.
	 * @param name
	 * @return Data
	 */
    public static Data createData(String name) {
        return new Data(name);
    }
}
