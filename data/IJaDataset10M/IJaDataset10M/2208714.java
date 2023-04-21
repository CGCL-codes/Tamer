package net.m2technologies.open_arm.metric;

import net.m2technologies.open_arm.transaction.UniqueObjectImpl;
import org.opengroup.arm40.metric.ArmMetricDefinition;
import org.opengroup.arm40.transaction.ArmID;
import org.opengroup.arm40.transaction.ArmTransactionFactory;

/**
 * Copyright 2005 Mark Masterson<br> <br> Licensed under the Apache License, Version 2.0 (the "License");<br> you may
 * not use this file except in compliance with the License.<br> You may obtain a copy of the License at<br> <br>
 * http://www.apache.org/licenses/LICENSE-2.0<br> <br> Unless required by applicable law or agreed to in writing,
 * software<br> distributed under the License is distributed on an "AS IS" BASIS,<br> WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied.<br> See the License for the specific language governing permissions and<br>
 * limitations under the License.<br>
 * <p/>
 * <p>Description: </p>
 *
 * @author Mark Masterson
 * @version 0.010
 */
public class ArmMetricDefinitionImpl extends UniqueObjectImpl implements ArmMetricDefinition {

    public ArmMetricDefinitionImpl(final ArmID id, final ArmTransactionFactory creatingFactory) {
        super(id, creatingFactory);
    }

    public String getUnits() {
        return null;
    }

    public short getUsage() {
        return 0;
    }
}
