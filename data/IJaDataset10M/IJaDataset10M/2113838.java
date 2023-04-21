package org.gwings.client.ui;

/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * Copyright 2007 Marcelo Emanoel B. Diniz <marceloemanoel AT gmail.com>
 *
 * @author Marcelo Emanoel
 * @since 08/03/2007
 */
public interface BoundModel extends SourcesBoundModelEvents {

    /**
	 * Returns the start value of the model.
	 * @return
	 */
    public Object getStart();

    /**
	 * Returns the finish value of the model. 
	 * @return
	 */
    public Object getFinish();

    /**
	 * Returns the increment value of the model.
	 * @return
	 */
    public Object getIncrement();

    /**
	 * Returns the current value of the model.
	 * @return
	 */
    public Object getValue();

    /**
	 * Returns true if the model is limited by the start and finish values.
	 * @return
	 */
    public boolean isLimited();

    /**
	 * Sets the limit propertie of the model. 
	 * @param limited
	 */
    public void setLimited(boolean limited);

    /***
	 * Sets the start value of the model.
	 * @param start
	 */
    public void setStart(Object start);

    /**
	 * Sets the finish value of the model.
	 * @param finish
	 */
    public void setFinish(Object finish);

    /**
	 * Set the increment value of the model.
	 * @param increment
	 */
    public void setIncrement(Object increment);

    /**
	 * Set the value of the model.
	 * The value must be between the start and finish values.
	 * @param value
	 */
    public void setValue(Object value);

    /**
	 * Increment the current value if it is in the model range or is not limited.
	 */
    public void increment();

    /**
	 * Decrement the current value if it is in the model range or is not limited.
	 *
	 */
    public void decrement();

    /**
	 * Returns a string representation of the current value of the model.
	 * @return
	 */
    public String formatValue();
}
