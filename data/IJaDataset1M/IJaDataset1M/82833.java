package net.m2technologies.open_arm.metric;

import org.opengroup.arm40.metric.ArmMetricDefinition;
import org.opengroup.arm40.metric.ArmMetricGaugeFloat32;
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
public class ArmMetricGaugeFloat32Impl extends ArmMetricImpl implements ArmMetricGaugeFloat32 {

    public ArmMetricGaugeFloat32Impl(final ArmTransactionFactory creatingFactory, final ArmMetricDefinition metricDefinition) {
        super(creatingFactory, metricDefinition);
    }

    public float get() {
        return 0.0f;
    }

    public int set(final float value) {
        return 0;
    }
}
