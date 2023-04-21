package org.formaria.resource;

/**
 * An interface for feeeding pack the download status
 * <p> Copyright (c) Formaria Ltd., 2008, This software is licensed under
 * the GNU Public License (GPL), please see license.txt for more details. If
 * you make commercial use of this software you must purchase a commercial
 * license from Formaria.</p>
 * @author Luan O'Carroll
 */
public interface ResourceLoaderStatus {

    public void setProgress(int percent);

    public void setProgressMessage(String msg);

    public void updateFileProgress();
}
