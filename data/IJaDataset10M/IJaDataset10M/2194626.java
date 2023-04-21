package org.gems.designer;

/******************************************************************************
 * Copyright (c) 2005, 2006 Jules White.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jules White - initial API and implementation 
 ****************************************************************************/
public interface ModelAccessListener {

    public static final String EXTENSION_POINT = "org.gems.designer.dsml.modelaccesslistener";

    public void modelOpened(GemsEditor editor, ModelInstance inst);

    public void modelClosed(GemsEditor editor, ModelInstance inst);
}
