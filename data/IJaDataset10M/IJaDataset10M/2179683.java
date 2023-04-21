package ee.webAppToolkit.example.projectTimeTracking.domain;

import java.util.Date;
import ee.webAppToolkit.example.projectTimeTracking.converters.TimeConverter;
import ee.webAppToolkit.forms.Display;
import ee.webAppToolkit.forms.List;
import ee.webAppToolkit.forms.Text;
import ee.webAppToolkit.forms.Display.Type;
import ee.webAppToolkit.localization.LocalizedString;
import ee.webAppToolkit.parameters.annotations.Converter;
import ee.webAppToolkit.website.domain.DisplayAwareIdEntity;

public class Work extends DisplayAwareIdEntity {

    @Display(type = Type.HIDDEN)
    public Employee employee;

    @Display(type = Type.LIST, order = 0, label = @LocalizedString("work.projectComponent"))
    @List(defaultLabel = @LocalizedString("work.selectProjectComponent"))
    public ProjectComponent projectComponent;

    @Display(type = Type.TEXT, order = 1, label = @LocalizedString("work.role"))
    @Text(readonly = true)
    public Role role;

    @Display(type = Type.TIME, order = 2, label = @LocalizedString("work.start"))
    @Converter(TimeConverter.class)
    public Date start;

    @Display(type = Type.TIME, order = 3, label = @LocalizedString("work.end"))
    @Converter(TimeConverter.class)
    public Date end;
}
