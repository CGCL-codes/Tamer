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
 * The interface presented to user class objects.
 *
 */
public interface CoreUserInterface {

    /**
	 * Is the node still booting up.
	 * @return
	 */
    public boolean isInitializing();

    /**
	 * Uploads the given data to the network.  A DownloadSpec will be returned
	 * to the user upon success.
	 */
    public void UploadData(UserUploadInterface up);

    /**
	 * Download the data specified.  The downloaded data will be returned to the
	 * user upon success.
	 * @param ds
	 */
    public void DownloadData(UserDownloadInterface down);

    /**
	 * Return information about how many search specifications we know
	 * for this query.
	 * @param info The query.
	 */
    public void SearchSpecInfo(UserSearchSpecInfo info);

    /**
	 * upload some data with keywords (using search specifications).
	 * @param keywords
	 * @param data
	 */
    public void SaveSearch(UserKeywordUpload up);

    /**
	 * Search for data with the following keyword.
	 * @param keyword
	 */
    public void Search(UserKeywordSearch search);

    public void DecodeDownload(DownloadDecodeRequest data);

    public void DecodeUpload(UploadDecodeRequest data);

    public void DecodeSearch(SearchDecodeRequest data);

    public Object getUserSettings();

    public MyNodeInfo getMyData();

    public void Hello(NodeHello hello);
}
