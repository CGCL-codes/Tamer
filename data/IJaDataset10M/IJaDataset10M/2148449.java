package org.gems.designer.model.props;

import org.gems.designer.model.ModelObject;

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
public interface CustomPropertyEx extends CustomProperty {

    public void setOwner(ModelObject obj);
}
