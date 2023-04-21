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
 * Local information about a search store including the time
 * it was received.
 *
 */
public class LocalSearchData {

    private SearchData SearchData;

    private long StoreTime;

    public SearchData getSearchData() {
        return SearchData;
    }

    public void setSearchData(SearchData searchData) {
        SearchData = searchData;
    }

    public long getStoreTime() {
        return StoreTime;
    }

    public void setStoreTime(long storeTime) {
        StoreTime = storeTime;
    }
}
