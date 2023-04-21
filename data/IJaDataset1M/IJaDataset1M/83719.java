package net.sourceforge.turtleweed.cmd;

import java.util.List;

public interface IParamsValidator {

    IValidationResult validatePatams(List<String> params);
}
