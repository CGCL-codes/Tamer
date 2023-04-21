package org.plazmaforge.framework.config.object;

import java.util.List;

public interface IFormConfig extends IUIObjectConfig {

    String getFormType();

    void setFormType(String formType);

    List<IActionDescriptorConfig> getActionDescriptors();

    void setActionDescriptors(List<IActionDescriptorConfig> actionDescriptors);

    void addActionDescriptor(IActionDescriptorConfig actionDescriptor);

    void removeActionDescriptor(IActionDescriptorConfig actionDescriptor);

    List<IReportDescriptorConfig> getReportDescriptors();

    void setReportDescriptors(List<IReportDescriptorConfig> reportDescriptors);

    void addReportDescriptor(IReportDescriptorConfig reportDescriptor);

    void removeReportDescriptor(IReportDescriptorConfig reportDescriptor);

    IReportDescriptorConfig getReportDescriptor(IReportConfig report);

    IReportDescriptorConfig getReportDescriptorById(String reportId);

    void removeReportConfig(IReportConfig report);

    void addReportConfig(IReportConfig report);
}
