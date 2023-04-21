package org.pfyshnet.core;

/**
 *     This file is part of Pfyshnet.
 *
 *  Pfyshnet is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  Pfyshnet is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with Pfyshnet.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Used to request data to be retrieved (A data query).
 *
 */
public class RetrieveRequest {

    private Object EncodedReturn;

    private Object DataEncodeKey;

    private long Tag;

    /**
	 * The encoded return onion to attach the data to.
	 * @return
	 */
    public Object getEncodedReturn() {
        return EncodedReturn;
    }

    /**
	 * The encoded return onion to attach the data to.
	 * @param encodedReturn
	 */
    public void setEncodedReturn(Object encodedReturn) {
        EncodedReturn = encodedReturn;
    }

    /**
	 * The encryption key to encode the data with before
	 * return the data.
	 * @return
	 */
    public Object getDataEncodeKey() {
        return DataEncodeKey;
    }

    /**
	 * The encryption key to encode the data with before
	 * return the data.
	 * @param dataEncodeKey
	 */
    public void setDataEncodeKey(Object dataEncodeKey) {
        DataEncodeKey = dataEncodeKey;
    }

    /**
	 * The tag of the data  to retreive.
	 * @return
	 */
    public long getTag() {
        return Tag;
    }

    /**
	 * The tag of the data to retreive.
	 * @param tag
	 */
    public void setTag(long tag) {
        Tag = tag;
    }
}
