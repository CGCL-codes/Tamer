package de.andreavicentini.magicphoto.domain.descriptions;

import de.andreavicentini.magicphoto.domain.pictures.IDescription;

public interface IDescriptionParser {

    IDescription parse(String description);
}
